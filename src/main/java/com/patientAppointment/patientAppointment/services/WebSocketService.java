package com.patientAppointment.patientAppointment.services;

import com.patientAppointment.patientAppointment.dtos.AppointmentDtoResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final String TOPIC = "/topic/"; // Topic for broadcasting messages
    private final SimpMessagingTemplate messagingTemplate;

    WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String channel, AppointmentDtoResponse record) {
        messagingTemplate.convertAndSend(this.TOPIC + channel, record);
    }

    public void sendDeleteMessage(String channel, Long id) {
        messagingTemplate.convertAndSend(this.TOPIC + channel, id);
    }

}
