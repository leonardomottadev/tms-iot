package com.tms.data_service.service;

import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private String lastMessage = "Nenhuma mensagem recebida ainda.";

    public synchronized void updateMessage(String message) {
        this.lastMessage = message;
    }

    public synchronized String getLastMessage() {
        return lastMessage;
    }
}