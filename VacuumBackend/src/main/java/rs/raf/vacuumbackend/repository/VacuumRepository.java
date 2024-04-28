package rs.raf.vacuumbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.vacuumbackend.model.Vacuum;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VacuumRepository extends JpaRepository<Vacuum,Long> {
    List<Vacuum> findVacuumsByActiveAndNameAndAddedBy(boolean active, String name,Long addedBy);
    List<Vacuum> findVacuumsByActiveAndStatusAndAddedBy(boolean active, int status,Long addedBy);
    List<Vacuum> findVacuumsByActiveAndDateCreatedIsBeforeAndAddedBy(boolean active, Date b4,Long addedBy);
    List<Vacuum> findVacuumsByActiveAndDateCreatedIsAfterAndAddedBy(boolean active, Date after,Long addedBy);
    List<Vacuum> findVacuumsByActiveAndAddedBy(boolean active,Long addedBy);
    Optional<Vacuum> findVacuumsByActiveAndVacuumIdAndAddedBy(boolean active,Long id,Long addedBy);

    Optional<Vacuum> findVacuumsByActiveAndVacuumId(boolean active,Long id);
    List<Vacuum> findVacuumsByActiveTrueAndNextStatusExpectedTimeNotNull();

}
