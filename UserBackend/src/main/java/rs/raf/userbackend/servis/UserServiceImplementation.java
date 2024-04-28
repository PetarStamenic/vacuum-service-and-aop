package rs.raf.userbackend.servis;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import rs.raf.userbackend.dto.NewUserDTO;
import rs.raf.userbackend.dto.TokenRequestDTO;
import rs.raf.userbackend.dto.TokenResponseDTO;
import rs.raf.userbackend.dto.UserDTO;
import rs.raf.userbackend.jwt.TokenService;
import rs.raf.userbackend.mapper.UserMapper;
import rs.raf.userbackend.model.MyUser;
import rs.raf.userbackend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {
    private TokenService tokenService;
    private UserMapper userMapper;
    private UserRepository userRepository;

    public UserServiceImplementation(TokenService tokenService, UserMapper userMapper, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public TokenResponseDTO login(TokenRequestDTO dto) {
        Optional<MyUser> user = userRepository.findUserByUsernameAndPassword(dto.getUsername(),dto.getPassword());
        if(!user.isPresent())
            return null;
        Claims claims = Jwts.claims();
        claims.put("id",user.get().getUserId());
        claims.put("permissions",user.get().getPermissions());
        return new TokenResponseDTO(tokenService.generate(claims));
    }

    @Override
    public UserDTO register(NewUserDTO dto) {
        if(userRepository.findUserByUsername(dto.getUsername()).isPresent())
            return null;
        MyUser user = userMapper.NewUserDTOToUser(dto);
        MyUser u = userRepository.save(user);
        return userMapper.UserToUserDTO(user);
    }

    @Override
    public boolean delete(Long id) {
        Optional<MyUser> user = userRepository.findUserByUserId(id);
        if(!user.isPresent())
            return false;
        userRepository.delete(user.get());
        return true;
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        Optional<MyUser> user = userRepository.findUserByUserId(userDTO.getId());
        if(!user.isPresent())
            return null;
        MyUser u = user.get();
        u.setUsername(userDTO.getUsername());
        u.setEmail(userDTO.getEmail());
        u.setPermissions(userDTO.getPermissions());
        userRepository.save(u);
        return userMapper.UserToUserDTO(u);
    }

    @Override
    public List<UserDTO> listUsers() {
        List<MyUser> users = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();
        for(MyUser u :users){
            dtos.add(userMapper.UserToUserDTO(u));
        }
        return dtos;
    }
}
