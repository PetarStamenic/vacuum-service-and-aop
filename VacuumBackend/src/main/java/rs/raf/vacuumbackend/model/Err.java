package rs.raf.vacuumbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Err {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long errorId;
    private Long userId;
    private Long vacuumId;
    private int errorCode;
    private Long time;
}
