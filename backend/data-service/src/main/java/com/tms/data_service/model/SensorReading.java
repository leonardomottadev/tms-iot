package com.tms.data_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_readings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperature;
    
    private Double humidity;

    @Column(nullable = false)
    private LocalDateTime timestamp;    
}