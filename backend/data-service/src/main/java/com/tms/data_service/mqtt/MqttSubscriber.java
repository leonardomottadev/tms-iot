package com.tms.data_service.mqtt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tms.data_service.service.MqttService;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

import org.eclipse.paho.client.mqttv3.*;

@Component
@RequiredArgsConstructor
public class MqttSubscriber implements CommandLineRunner {

    private MqttClient client;
    private final MqttService mqttService;

    @Override
    public void run(String... args) throws Exception {
        String brokerUrl = "tcp://broker.emqx.io:1883";
        String clientId = "spring-mqtt-subscriber";
        String topic = "tms-iot/test/sensor-reader/payload";

        client = new MqttClient(brokerUrl, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        client.connect(options);
        System.out.println("Conectado ao broker EMQX");

        client.subscribe(topic, (t, msg) -> {
            String payload = new String(msg.getPayload());
            System.out.println("Mensagem recebida: " + payload);
            mqttService.updateMessage(payload);
        });
    }

    @PreDestroy
    public void closeConnection() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
            client.close();
            System.out.println("Conexão MQTT encerrada.");
        }
    }
}
