package RBPO.proj.repository;

import RBPO.proj.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPetId(Long petId);

    List<Appointment> findByVetId(Long vetId);

    boolean existsByVetIdAndDateTime(Long vetId, LocalDateTime dateTime);

    boolean existsByVetIdAndDateTimeAndIdNot(Long vetId, LocalDateTime dateTime, Long id);

    List<Appointment> findByVetIdAndDateTimeBetweenOrderByDateTimeAsc(Long vetId, LocalDateTime from, LocalDateTime to);

    long countByVetIdAndDateTimeBetween(Long vetId, LocalDateTime from, LocalDateTime to);
}
