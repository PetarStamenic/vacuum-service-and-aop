package rs.raf.vacuumbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
public class SearchDTO {
    private String name;
    private List<Integer> status;
    private Date dateFrom;
    private Date dateTo;
}
