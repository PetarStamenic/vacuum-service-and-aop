package rs.raf.vacuumbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacuumDTO {
    Long id;
    String name;
    int status;
    Long dateOfProduction;
}
