package rs.raf.userbackend.servis;

import rs.raf.userbackend.dto.NewUserDTO;
import rs.raf.userbackend.dto.TokenRequestDTO;
import rs.raf.userbackend.dto.TokenResponseDTO;
import rs.raf.userbackend.dto.UserDTO;

import java.util.List;

public interface UserService {
    TokenResponseDTO login(TokenRequestDTO dto);
    UserDTO register(NewUserDTO dto);
    boolean delete(Long id);
    UserDTO update(UserDTO userDTO);
    List<UserDTO> listUsers();
}
