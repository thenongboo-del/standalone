package com.sbproject.standalone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sbproject.standalone.entity.ChatResponseDto;
import com.sbproject.standalone.service.FastApiClientService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final FastApiClientService fastApiClientService;

    @GetMapping("/chat")
    public String chatForm() {
        return "chat"; // 
    }

    @PostMapping("/chat")
    public String sendChat(@RequestParam String message,
                           @RequestParam(required = false) String session_id,
                           Model model) {

        // Mono을 block()하여 간단하게 동기 처리 (UI/Thymeleaf용)
        ChatResponseDto resp = fastApiClientService.sendMessage(message, session_id).block();

        // 응답 내용 Thymeleaf에 전달
        model.addAttribute("question", message);
        model.addAttribute("answer", resp != null ? resp.getReply() : "응답 없음");
        model.addAttribute("sessionId", resp != null ? resp.getSession_id() : null);

        return "chat"; // 
    }
}