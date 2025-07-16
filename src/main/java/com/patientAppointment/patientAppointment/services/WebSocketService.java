package com.patientAppointment.patientAppointment.services;

import com.patientAppointment.patientAppointment.dtos.AppointmentDtoResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

public class WebSocketService {

    private final String TOPIC = "/topic/patientCreated"; // Topic for broadcasting messages
    private final SimpMessagingTemplate messagingTemplate;

    WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(AppointmentDtoResponse record) {
        messagingTemplate.convertAndSend(this.TOPIC, record);
    }
}
