package rs.raf.userbackend.mapper;

import org.springframework.stereotype.Component;
import rs.raf.userbackend.dto.NewUserDTO;
import rs.raf.userbackend.dto.UserDTO;
import rs.raf.userbackend.model.MyUser;

@Component
public class UserMapper {

    public UserDTO UserToUserDTO(MyUser user){
        if (user.getPermissions() >15 || user.getPermissions()< 0)
            new UserDTO(user.getUserId(),user.getUsername(),user.getEmail(), (byte)0b0000);
        return new UserDTO(user.getUserId(),user.getUsername(),user.getEmail(), user.getPermissions());
    }
    public MyUser NewUserDTOToUser(NewUserDTO dto){
        if (dto.getPermissions() >15 || dto.getPermissions()< 0)
            new MyUser(null,dto.getUsername(),dto.getPassword(),dto.getEmail(), (byte)0b0000);
        return new MyUser(null,dto.getUsername(),dto.getPassword(),dto.getEmail(), dto.getPermissions());
    }
}
