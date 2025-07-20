package com.patientAppointment.patientAppointment.controllers;

import com.patientAppointment.patientAppointment.dtos.AppointmentDtoRequest;
import com.patientAppointment.patientAppointment.dtos.WhatsappWebhookDto;
import com.patientAppointment.patientAppointment.dtos.WhatsappWebhookSession;
import com.patientAppointment.patientAppointment.services.PatientService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/webhook")
public class WhatsappWebhookController {
    private final String PHONE_NUMBER_ID = "646658418541683";
    private final String ACCESS_TOKEN = "EAAQPNnAl6IsBPAO3nlq13kmrEtuumCYbgG8Lr644JewWNhLp9rJspPnt6EwkGZBgFzxeQYeSISssX93RGkwUY8PP4RhVrKfPx0uZALHGcGSzWXGDmK5wLuZBsFmPMsOWFBdXwuzqDyYwPwdjOUJSqQYnSZC4fWqnntjRmDgFYYNBZBrUxksj6FgjLbqD0JJMOQdENXj9lghHZAfyI8QQyr1yPYPgZAZCmClbo2ccbQGcygZDZD";

    private final Map<String, WhatsappWebhookSession> sessions = new HashMap<>();

    PatientService patientService;

    WhatsappWebhookController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public void handleIncomingMessage(@RequestBody WhatsappWebhookDto message) {
        String phone = message.getFrom();
        String text = message.getText().getBody().trim();

        WhatsappWebhookSession session = sessions.getOrDefault(phone, new WhatsappWebhookSession());

        switch (session.getStep()) {
            case 1:
                session.setFirstName(text);
                session.setStep(2);
                sendMessage(phone, "Great! Please provide the last name.");
                break;

            case 2:
                session.setLastName(text);
                session.setStep(2);
                sendMessage(phone, "Great! Please provide the date.");
                break;

            case 3:
                session.setDate(text);
                session.setStep(4);
                sendMessage(phone, "Almost done! At what time?");
                break;

            case 4:
                session.setTime(text);
                session.setStep(5);
                sendMessage(phone, "Please confirm:\n" +
                        "First Name: " + session.getFirstName() + "\n" +
                        "Last Name: " + session.getLastName() + "\n" +
                        "Date: " + session.getDate() + "\n" +
                        "Time: " + session.getTime() +
                        "\n\nReply YES to confirm or NO to cancel.");
                break;

            case 5:
                if (text.equalsIgnoreCase("yes")) {
                    callAppointmentAPI(session, phone);
                    sendMessage(phone, "✅ Appointment confirmed!");
                    sessions.remove(phone);
                } else {
                    sendMessage(phone, "❌ Booking cancelled. Send 'Hi' to start again.");
                    sessions.remove(phone);
                }
                break;

            default:
                session.setStep(1);
                sendMessage(phone, "Hi! 👋 Let's book your appointment.\nWhat is your first name?");
        }

        sessions.put(phone, session);
    }

    private void sendMessage(String phone, String body) {
        // Use WhatsApp Cloud API to send message to `phone` with `body`
        String url = "https://graph.facebook.com/v19.0/" + PHONE_NUMBER_ID + "/messages";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = new HashMap<>();
        payload.put("messaging_product", "whatsapp");
        payload.put("to", phone);
        payload.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", body);
        payload.put("text", text);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        restTemplate.postForEntity(url, entity, String.class);
        System.out.println("Sending message to " + phone + ": " + body);
    }

    private void callAppointmentAPI(WhatsappWebhookSession session, String phone) {
        // Logic to call your backend appointment booking service
//        System.out.println("Calling API for " + session.getFullName());
        AppointmentDtoRequest appointmentDtoRequest = new AppointmentDtoRequest();
        appointmentDtoRequest.setFirstName(session.getFirstName());
        appointmentDtoRequest.setLastName(session.getLastName());
        appointmentDtoRequest.setPhoneNo(phone);
        appointmentDtoRequest.setDateTime(session.getDate() + " " + session.getTime());
        appointmentDtoRequest.setStatus("Booked");
        this.patientService.createPatient(appointmentDtoRequest);
    }
}
