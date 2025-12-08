package com.sbproject.standalone.controller;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.sbproject.standalone.entity.Consultation;
import com.sbproject.standalone.entity.ConsultationStatus;
import com.sbproject.standalone.repository.ConsultationRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ConsultationRepository consultationRepo;
    private final Logger log = LoggerFactory.getLogger(ScheduleController.class);
    private static final DateTimeFormatter ISO_OFFSET_FMT = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @GetMapping
    public List<Map<String, Object>> getEvents(@RequestParam("start") String startIso,
                                               @RequestParam("end") String endIso) {
        log.info("Schedule request start={} end={}", startIso, endIso);

        ZonedDateTime startZ = parseToZoned(startIso);
        ZonedDateTime endZ = parseToZoned(endIso);

        if (startZ == null || endZ == null) {
            log.warn("Failed to parse start or end -> returning empty list");
            return Collections.emptyList();
        }

        // DB는 LocalDateTime으로 저장돼 있으므로 LocalDateTime 범위로 변환
        LocalDateTime startLocal = startZ.toLocalDateTime();
        LocalDateTime endLocal = endZ.toLocalDateTime();

        List<Consultation> results = consultationRepo.findByNextScheduleBetweenAndStatusNot(startLocal, endLocal, ConsultationStatus.END);
        if (results == null || results.isEmpty()) {
            log.info("No consultations found in range");
            return Collections.emptyList();
        }

        List<Map<String, Object>> events = results.stream().map(c -> {
            Map<String, Object> ev = new HashMap<>();
            ev.put("id", String.valueOf(c.getId()));

            // title: 고객ID + 타입
            String title = (c.getCustomerId() != null ? c.getCustomerId() : "고객")
                    + (c.getType() != null ? " (" + c.getType().name() + ")" : "");
            ev.put("title", title);

            // start: nextSchedule 우선, 없으면 requestedAt 사용 (둘 다 LocalDateTime)
            LocalDateTime dt = c.getNextSchedule() != null ? c.getNextSchedule() : c.getRequestedAt();
            if (dt != null) {
                ZonedDateTime zdt = dt.atZone(ZoneId.systemDefault());
                // FullCalendar가 이해하는 오프셋 포함 ISO 문자열
                ev.put("start", zdt.format(ISO_OFFSET_FMT));
                // optional: end (예: 1시간 슬롯)
                ev.put("end", zdt.plusHours(1).format(ISO_OFFSET_FMT));
            } else {
                ev.put("start", null);
            }

            Map<String, Object> ext = new HashMap<>();
            ext.put("type", c.getType() != null ? c.getType().name() : null);
            ext.put("customerId", c.getCustomerId());
            ext.put("status", c.getStatus() != null ? c.getStatus().name() : null);
            ev.put("extendedProps", ext);

            return ev;
        }).collect(Collectors.toList());

        log.info("Returning {} events", events.size());
        return events;
    }

    private ZonedDateTime parseToZoned(String iso) {
        if (iso == null) return null;
        // 1) OffsetDateTime (Z 또는 +09:00 포함) 시도
        try {
            OffsetDateTime odt = OffsetDateTime.parse(iso);
            return odt.toZonedDateTime();
        } catch (DateTimeParseException ignored) {}

        // 2) LocalDateTime (YYYY-MM-DDTHH:MM:SS) 시도
        try {
            LocalDateTime ldt = LocalDateTime.parse(iso);
            return ldt.atZone(ZoneId.systemDefault());
        } catch (DateTimeParseException ignored) {}

        // 3) LocalDate (YYYY-MM-DD) 시도 -> 시작 자정으로 변환
        try {
            LocalDate ld = LocalDate.parse(iso);
            return ld.atStartOfDay(ZoneId.systemDefault());
        } catch (DateTimeParseException e) {
            log.error("Failed to parse date string: {}", iso);
            return null;
        }
    }
}
