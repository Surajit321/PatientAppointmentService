package com.patientAppointment.patientAppointment.repositories;

import com.patientAppointment.patientAppointment.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Additional query methods can be defined here if needed
}
