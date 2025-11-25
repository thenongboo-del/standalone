package com.sbproject.standalone.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class CarDetailInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Car와 1:1 관계
    @OneToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    // 상세 정보 컬럼
    private String model;
    private String engine;
    private String fuel;
    private String transmission;   // 변속기
    private String maxPower;       // 최고출력
    private String maxTorque;      // 최대토크
    private String displacement;   // 배기량
    private String driveType;      // 구동방식
    private String fuelTank;       // 연료탱크 용량
    private String batteryType;    // 배터리 종류
    private String batteryCapacity;// 배터리 용량
    private String evRange;        // 1회 충전 주행거리
    private String fastChargeTime; // 충전시간(급속)
    private String slowChargeTime; // 충전시간(완속)
    private String curbWeight;     // 공차중량
}
