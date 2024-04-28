package rs.raf.userbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDTO {
    String username;
    String password;
}
