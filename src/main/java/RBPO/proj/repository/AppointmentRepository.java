package RBPO.proj.repository;

import RBPO.proj.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPetId(Long petId);

    List<Appointment> findByVetId(Long vetId);

    boolean existsByVetIdAndVisitDate(Long vetId, LocalDate visitDate);

    boolean existsByVetIdAndVisitDateAndIdNot(Long vetId, LocalDate visitDate, Long id);

    List<Appointment> findByVetIdAndVisitDateBetweenOrderByVisitDateAsc(Long vetId, LocalDate from, LocalDate to);
}
