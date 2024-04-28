package rs.raf.vacuumbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.vacuumbackend.model.Err;

import java.util.List;

public interface ErrRepository extends JpaRepository<Err,Long> {

    List<Err> findErrsByUserId(Long userId);
}
