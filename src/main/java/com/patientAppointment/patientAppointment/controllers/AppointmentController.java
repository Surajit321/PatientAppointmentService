package com.patientAppointment.patientAppointment.controllers;

import com.patientAppointment.patientAppointment.dtos.AppointmentDtoRequest;
import com.patientAppointment.patientAppointment.dtos.AppointmentDtoResponse;
import com.patientAppointment.patientAppointment.services.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final PatientService patientService;

    AppointmentController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patients")
    public List<AppointmentDtoResponse> getPatients() {
        return this.patientService.getAllPatients();
    }

    @PostMapping("/patients")
    public AppointmentDtoResponse createPatient(@RequestBody AppointmentDtoRequest patientDetails) {
            return this.patientService.createPatient(patientDetails);
    }

    @DeleteMapping("/patients")
    public Boolean deletePatient() {
        return this.patientService.deleteAllPatient();
    }

    @PutMapping("/patients/{id}")
    public void updatePatient(@PathVariable("id") Long id, @RequestBody AppointmentDtoRequest patientDetails) {
        this.patientService.updatePatient(id, patientDetails);
    }


}
