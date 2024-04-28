package rs.raf.userbackend.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.userbackend.dto.NewUserDTO;
import rs.raf.userbackend.dto.TokenRequestDTO;
import rs.raf.userbackend.dto.TokenResponseDTO;
import rs.raf.userbackend.dto.UserDTO;
import rs.raf.userbackend.jwt.ROLES;
import rs.raf.userbackend.jwt.jwt;
import rs.raf.userbackend.servis.UserService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8081"})
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid TokenRequestDTO dto){
        TokenResponseDTO tokenResponseDTO = userService.login(dto);
        if(tokenResponseDTO  == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(tokenResponseDTO,HttpStatus.OK);
    }
    @GetMapping("/canUserSearch")
    @jwt(permisions = ROLES.CAN_SEARCH_VACUUM)
    public ResponseEntity<Objects> canUserSearch(@RequestHeader("Authorization")String authorization){
        return new ResponseEntity(null,HttpStatus.OK);
    }
    @GetMapping("/canUserStart")
    @jwt(permisions = ROLES.CAN_START_VACUUM)
    public ResponseEntity<Objects> canUserStart(@RequestHeader("Authorization")String authorization){
        return new ResponseEntity(null,HttpStatus.OK);
    }
    @GetMapping("/canUserStop")
    @jwt(permisions = ROLES.CAN_STOP_VACUUM)
    public ResponseEntity<Objects> canUserStop(@RequestHeader("Authorization")String authorization){
        return new ResponseEntity(null,HttpStatus.OK);
    }
    @GetMapping("/canUserDischarge")
    @jwt(permisions = ROLES.CAN_DISCHARGE_VACUUM)
    public ResponseEntity<Objects> canUserDischarge(@RequestHeader("Authorization")String authorization){
        return new ResponseEntity(null,HttpStatus.OK);
    }
    @GetMapping("/canUserAdd")
    @jwt(permisions = ROLES.CAN_ADD_VACUUM)
    public ResponseEntity<Objects> canUserAdd(@RequestHeader("Authorization")String authorization){
        return new ResponseEntity(null,HttpStatus.OK);
    }
    @GetMapping("/canUserRemove")
    @jwt(permisions = ROLES.CAN_REMOVE_VACUUM)
    public ResponseEntity<Objects> canUserRemove(@RequestHeader("Authorization")String authorization){
        return new ResponseEntity(null,HttpStatus.OK);
    }
    @GetMapping("/listAll")
    @jwt(permisions = ROLES.CAN_READ_USERS)
    public ResponseEntity<List<UserDTO>> listUsers(@RequestHeader("Authorization")String authorization){
        return new ResponseEntity<>(userService.listUsers(), HttpStatus.OK);
    }

    @PutMapping("/register")
    @jwt(permisions = ROLES.CAN_CREATE_USERS)
    public ResponseEntity<UserDTO> register(@RequestBody @Valid NewUserDTO newUserDTO,@RequestHeader("Authorization")String authorization){
        UserDTO userDTO = userService.register(newUserDTO);
        if(userDTO == null)
            return new ResponseEntity<>(userDTO,HttpStatus.CONFLICT);
        return new ResponseEntity<>(userDTO,HttpStatus.CREATED);
    }

    @PostMapping("/update")
    @jwt(permisions = ROLES.CAN_UPDATE_USERS)
    public ResponseEntity<UserDTO> update(@RequestBody @Valid UserDTO userDTO,@RequestHeader("Authorization")String authorization){
        return new ResponseEntity<>(userService.update(userDTO),HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    @jwt(permisions = ROLES.CAN_DELETE_USERS)
    public ResponseEntity<String > delete(@PathVariable(value = "id")Long id,@RequestHeader("Authorization")String authorization){
        if(userService.delete(id))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
