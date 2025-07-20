package com.patientAppointment.patientAppointment.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhatsappWebhookSession {
    private int step = 1;
    private String firstName;
    private String lastName;
//    private String doctorName;
//    private String hospital;
    private String date;
    private String time;
}
