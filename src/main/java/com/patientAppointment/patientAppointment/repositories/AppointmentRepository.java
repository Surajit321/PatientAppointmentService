package com.patientAppointment.patientAppointment.repositories;

import com.patientAppointment.patientAppointment.dtos.AppointmentDtoResponse;
import com.patientAppointment.patientAppointment.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Additional query methods can be defined here if needed

    @Modifying
    @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
    public void updateStatusById(@Param("id") Long id, @Param("status") String status);
}
