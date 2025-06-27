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

    private final String brokerUrl = "tcp://broker.emqx.io:1883";
    private final String clientId = "spring-mqtt-subscriber";
    private final String topic = "tms-iot/test/sensor-reader/payload";

    private volatile boolean running = true;

    @Override
    public void run(String... args) throws Exception {
        connectAndSubscribe();
    }

    private void connectAndSubscribe() {
        new Thread(() -> {
            while (running) {
                try {
                    if (client == null || !client.isConnected()) {
                        client = new MqttClient(brokerUrl, clientId);
                        MqttConnectOptions options = new MqttConnectOptions();
                        options.setAutomaticReconnect(false);
                        options.setCleanSession(true);

                        client.setCallback(new MqttCallback() {
                            @Override
                            public void connectionLost(Throwable cause) {
                                System.err.println("Conexão perdida: " + cause.getMessage());
                                retryConnection();
                            }

                            @Override
                            public void messageArrived(String topic, MqttMessage message) throws Exception {
                                String payload = new String(message.getPayload());
                                System.out.println("Mensagem recebida: " + payload);
                                mqttService.updateMessage(payload);
                            }

                            @Override
                            public void deliveryComplete(IMqttDeliveryToken token) {
                                System.out.println("Delivery Completed.");
                            }
                        });

                        client.connect(options);
                        System.out.println("Conectado ao broker EMQX");
                        client.subscribe(topic);
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.err.println("Erro ao conectar: " + e.getMessage());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }

    private void retryConnection() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        } catch (MqttException ignored) { 
            System.out.println("Erro ao reconectar MQTT.");
        }
    }

    @PreDestroy
    public void closeConnection() throws MqttException {
        running = false;
        if (client != null && client.isConnected()) {
            client.disconnect();
            client.close();
            System.out.println("Conexão MQTT encerrada.");
        }
    }
}
