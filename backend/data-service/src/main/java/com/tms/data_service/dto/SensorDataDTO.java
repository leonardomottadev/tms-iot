package com.tms.data_service.dto;

public class SensorDataDTO {
    private double temperature;
    private double humidity;
    private String timestamp;

    // Getters e Setters
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
