package RBPO.proj.repository;

import RBPO.proj.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    boolean existsByAppointmentId(Long appointmentId);

    void deleteByAppointmentId(Long appointmentId);

    Optional<Treatment> findByAppointmentId(Long appointmentId);
}
