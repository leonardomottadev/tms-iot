package com.tms.data_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.data_service.dto.SensorDataDTO;
import com.tms.data_service.service.MqttService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "Sensors", description = "Sensor data retrieval")
@RequiredArgsConstructor
public class SensorController {

    private final MqttService mqttService;
    private final ObjectMapper objectMapper;
    
    @GetMapping("/sensor")
    @Operation(
        summary = "Get sensor data",
        description = "Returns the last received sensor data."
    )
    @SecurityRequirement(name = "bearerAuth")
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