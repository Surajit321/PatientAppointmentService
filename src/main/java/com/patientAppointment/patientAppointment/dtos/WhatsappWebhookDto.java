package com.patientAppointment.patientAppointment.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhatsappWebhookDto {
    private String from;
    private Text text;

    @Getter
    @Setter
    public static class Text {
        private String body;
    }
}
