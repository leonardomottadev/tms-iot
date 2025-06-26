package com.tms.data_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataDTO {
    private double temperature;
    private double humidity;
    private String timestamp;
}
