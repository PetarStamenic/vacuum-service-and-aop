package rs.raf.vacuumbackend.mapper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rs.raf.vacuumbackend.model.Vacuum;
import rs.raf.vacuumbackend.repository.VacuumRepository;
import rs.raf.vacuumbackend.staticNumers.STATUSES;

import java.util.Date;

@Profile({"default"})
@Component
public class VacuumSeeder implements CommandLineRunner {
    
    private VacuumRepository vacuumRepository;

    public VacuumSeeder(VacuumRepository vacuumRepository) {
        this.vacuumRepository = vacuumRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Vacuum vacuum1 = new Vacuum();
        vacuum1.setName("vacum");
        vacuum1.setActive(true);
        vacuum1.setStatus(STATUSES.STOP);
        vacuum1.setDateCreated(new Date(System.currentTimeMillis()));
        vacuum1.setAddedBy(1L);
        vacuumRepository.save(vacuum1);


        Vacuum vacuum2 = new Vacuum();
        vacuum2.setName("vacom");
        vacuum2.setActive(true);
        vacuum2.setStatus(STATUSES.START);
        vacuum2.setDateCreated(new Date(System.currentTimeMillis()));
        vacuum2.setAddedBy(1L);
        vacuumRepository.save(vacuum2);
    }
}
