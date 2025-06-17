package com.tms.data_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.data_service.dto.SensorDataDTO;
import com.tms.data_service.service.MqttService;

@RestController
public class SensorController {

    private final MqttService mqttService;
    private final ObjectMapper objectMapper;

    public SensorController(MqttService mqttService, ObjectMapper objectMapper) {
        this.mqttService = mqttService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/sensor")
    public SensorDataDTO getSensorData() {
        try {
            String raw = mqttService.getLastMessage();

            if (raw == null || raw.isBlank()) {
                return new SensorDataDTO();
            }

            return objectMapper.readValue(raw, SensorDataDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new SensorDataDTO();
        }
    }
}