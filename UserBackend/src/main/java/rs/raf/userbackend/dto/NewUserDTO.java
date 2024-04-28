package rs.raf.userbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDTO {
        String username;
        String password;
        String email;
        int permissions;
}
