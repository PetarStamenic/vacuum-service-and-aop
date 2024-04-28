package rs.raf.vacuumbackend.mapper;

import org.springframework.stereotype.Component;
import rs.raf.vacuumbackend.dto.NewVacuumDTO;
import rs.raf.vacuumbackend.dto.VacuumDTO;
import rs.raf.vacuumbackend.model.Vacuum;
import rs.raf.vacuumbackend.staticNumers.STATUSES;

import java.util.Date;

@Component
public class VacuumMapper {

    public Vacuum NewVacuumDTOToVacuum(NewVacuumDTO newVacuumDTO,Long userId){
        Vacuum vacuum = new Vacuum();
        vacuum.setVacuumId(null);
        vacuum.setName(newVacuumDTO.getName());
        vacuum.setActive(true);
        vacuum.setStatus(STATUSES.STOP);
        vacuum.setAddedBy(userId);
        vacuum.setDateCreated(new Date(newVacuumDTO.getDateCreated()));
        vacuum.setNextStatus(null);
        vacuum.setNextStatusExpectedTime(null);
        vacuum.setNumberOfCycles(0);
        return vacuum;
    }

    public VacuumDTO VacuumToVacuumDTO(Vacuum vacuum){
        VacuumDTO vacuumDTO = new VacuumDTO();
        vacuumDTO.setId(vacuum.getVacuumId());
        vacuumDTO.setName(vacuum.getName());
        vacuumDTO.setStatus(vacuum.getStatus());
        vacuumDTO.setDateOfProduction(vacuum.getDateCreated().getTime());
        return vacuumDTO;
    }

}
