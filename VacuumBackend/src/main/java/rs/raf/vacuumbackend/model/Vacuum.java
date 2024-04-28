package rs.raf.vacuumbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vacuum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacuumId;
    private String name;
    private int status;
    private Long addedBy;
    private boolean active;
    private Date dateCreated;
    private Integer nextStatus;
    private Long nextStatusExpectedTime;
    private int numberOfCycles;

}
