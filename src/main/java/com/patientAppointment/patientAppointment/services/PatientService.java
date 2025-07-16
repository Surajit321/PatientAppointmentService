package com.patientAppointment.patientAppointment.services;

import com.patientAppointment.patientAppointment.dtos.AppointmentDtoRequest;
import com.patientAppointment.patientAppointment.dtos.AppointmentDtoResponse;
import com.patientAppointment.patientAppointment.models.Appointment;
import com.patientAppointment.patientAppointment.repositories.AppointmentRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final AppointmentRepository appointmentRepository;

    private final WebSocketService webSocketService;

    PatientService(AppointmentRepository appointmentRepository, WebSocketService webSocketService){
        this.appointmentRepository = appointmentRepository;
        this.webSocketService = webSocketService;
    }

    public List<AppointmentDtoResponse> getAllPatients() {
        // Logic to get all patients
        return this.appointmentRepository.findAll()
                .stream()
                .map(this::convertToPatientDto)
                .toList();
    }

    public AppointmentDtoResponse createPatient(AppointmentDtoRequest patientDetails) {
        // Logic to create a new patient
        Appointment savedAppointment = this.appointmentRepository.save(this.convertToAppointment(patientDetails));
        AppointmentDtoResponse appointmentDtoResponse =  this.convertToPatientDto(savedAppointment);
        this.webSocketService.sendMessage("patientCreated", appointmentDtoResponse);
        return appointmentDtoResponse;
    }

    public Boolean deleteAllPatient() {
        // Logic to delete all patients
        return true; // Placeholder return statement
    }

    @Transactional
    public void updatePatient(Long id, AppointmentDtoRequest patientDetails) {
        // Logic to update a patient
        String currentStatus = patientDetails.getStatus();
        if(currentStatus.equals("Booked")){
            currentStatus = "Open";
            this.appointmentRepository.updateStatusById(id, currentStatus);
            patientDetails.setStatus(currentStatus);
            AppointmentDtoResponse appointmentDtoResponse = convertToPatientDto(convertToAppointment(patientDetails));
            appointmentDtoResponse.setId(id);
            this.webSocketService.sendMessage("patientUpdated", appointmentDtoResponse);
        }else {
            this.appointmentRepository.deleteById(id);
            this.webSocketService.sendDeleteMessage("patientDeleted", id);
        }
        // Placeholder return statement
    }


    private Appointment convertToAppointment(AppointmentDtoRequest patientDetails) {
        Appointment appointment = new Appointment();
        // Map fields from patientDetails to appointment
        appointment.setFirstName(patientDetails.getFirstName());
        appointment.setLastName(patientDetails.getLastName());
        appointment.setStatus(patientDetails.getStatus());
        appointment.setDateTime(patientDetails.getDateTime());
        appointment.setPhoneNo(patientDetails.getPhoneNo());

        return appointment;
    }

    private AppointmentDtoResponse convertToPatientDto(Appointment appointment) {
        AppointmentDtoResponse patientDtoResponse = new AppointmentDtoResponse();
        // Map fields from appointment to patientDto
        patientDtoResponse.setId(appointment.getId());
        patientDtoResponse.setFirstName(appointment.getFirstName());
        patientDtoResponse.setLastName(appointment.getLastName());
        patientDtoResponse.setStatus(appointment.getStatus());
        patientDtoResponse.setPhoneNo(appointment.getPhoneNo());
        patientDtoResponse.setDateTime(appointment.getDateTime());
        return patientDtoResponse;
    }

}
