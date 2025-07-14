package com.patientAppointment.patientAppointment.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDtoRequest {
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String dateTime;
    private String status;
}
