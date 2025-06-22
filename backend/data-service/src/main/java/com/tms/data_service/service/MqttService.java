package com.tms.data_service.service;

import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private String lastMessage = "{\"temperature\": 0.0, \"humidity\": 0.0, \"timestamp\": null}";

    public synchronized void updateMessage(String message) {
        this.lastMessage = message;
    }

    public synchronized String getLastMessage() {
        return lastMessage;
    }
}