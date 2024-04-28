package rs.raf.vacuumbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.vacuumbackend.dto.NewVacuumDTO;
import rs.raf.vacuumbackend.dto.SearchDTO;
import rs.raf.vacuumbackend.dto.VacuumDTO;
import rs.raf.vacuumbackend.jwt.jwt;
import rs.raf.vacuumbackend.mapper.VacuumMapper;
import rs.raf.vacuumbackend.model.Err;
import rs.raf.vacuumbackend.model.Vacuum;
import rs.raf.vacuumbackend.service.TokenService;
import rs.raf.vacuumbackend.service.VacuumService;
import rs.raf.vacuumbackend.staticNumers.ERRORS;
import rs.raf.vacuumbackend.staticNumers.ROLES;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/vacuum")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
public class VacuumController {
    private VacuumService vacuumService;
    private TokenService tokenService;
    private VacuumMapper vacuumMapper;

    public VacuumController(VacuumService vacuumService, TokenService tokenService, VacuumMapper vacuumMapper) {
        this.vacuumService = vacuumService;
        this.tokenService = tokenService;
        this.vacuumMapper = vacuumMapper;
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<VacuumDTO>> listAll(@RequestHeader("Authorization")String authorization) throws ExecutionException, InterruptedException {
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        Future<List<Vacuum>> vacuums = vacuumService.findAllByUser(userId);
        List<VacuumDTO> dtos = new ArrayList<>();
        while (true){
            if(vacuums.isDone()) {
                for (Vacuum vac : vacuums.get()) {
                    dtos.add(vacuumMapper.VacuumToVacuumDTO(vac));
                }
                return new ResponseEntity<>(dtos,HttpStatus.OK);
            }
            Thread.sleep(500);
        }
    }

    @GetMapping("/listErr")
    public ResponseEntity<List<Err>> listErr(@RequestHeader("Authorization")String authorization) throws ExecutionException, InterruptedException {
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        Future<List<Err>> errs = vacuumService.findAllErrorsByUser(userId);
        while (true){
            if(errs.isDone()) {
                return new ResponseEntity<>(errs.get(),HttpStatus.OK);
            }
            Thread.sleep(500);
        }
    }


    @PostMapping("/search")
    @jwt(permisions = ROLES.CAN_SEARCH_VACUUM)
    public ResponseEntity<List<VacuumDTO>> searchVacuum(@RequestHeader("Authorization")String authorization, @RequestBody @Valid SearchDTO search) throws ExecutionException, InterruptedException {
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        Future<List<Vacuum>> vacuums = vacuumService.search(search.getName(),search.getStatus(),search.getDateFrom(),search.getDateTo(),userId);
        List<VacuumDTO> dtos = new ArrayList<>();
        while (true){
            if(vacuums.isDone()) {
                for (Vacuum vac : vacuums.get()) {
                    dtos.add(vacuumMapper.VacuumToVacuumDTO(vac));
                }
                return new ResponseEntity<>(dtos,HttpStatus.OK);
            }
            Thread.sleep(500);
        }
    }

    @GetMapping("/start/{id}")
    @jwt(permisions = ROLES.CAN_START_VACUUM)
    public ResponseEntity<String> startVacuum(@PathVariable(value = "id")Long id,@RequestHeader("Authorization")String authorization) throws ExecutionException, InterruptedException {
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        Future<Integer> futint = vacuumService.start(id,userId,null);
        Integer sw;
        while (true){
            if(futint.isDone()){
                sw = futint.get();
                break;
            }
            Thread.sleep(500);
        }
        switch (sw){
            case ERRORS.NOERROR:{
                return new ResponseEntity<>(HttpStatus.OK);
            }
            case ERRORS.NOTFOUND:{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            case ERRORS.OCCUPIED:{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            case ERRORS.ALREADYON:{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        }
        return null;
    }

    @GetMapping("/stop/{id}")
    @jwt(permisions = ROLES.CAN_STOP_VACUUM)
    public ResponseEntity<String> stopVacuum(@PathVariable(value = "id")Long id,@RequestHeader("Authorization")String authorization) throws ExecutionException, InterruptedException {
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        Future<Integer> futint = vacuumService.stop(id,userId,null);
        Integer sw;
        while (true){
            if(futint.isDone()){
                sw = futint.get();
                break;
            }
            Thread.sleep(500);
        }
        switch (sw){
            case ERRORS.NOERROR:{
                return new ResponseEntity<>(HttpStatus.OK);
            }
            case ERRORS.NOTFOUND:{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            case ERRORS.OCCUPIED:{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            case ERRORS.ALREADYOFF:{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        }
        return null;
    }

    @GetMapping("/discharge/{id}")
    @jwt(permisions = ROLES.CAN_DISCHARGE_VACUUM)
    public ResponseEntity<String> dischargeVacuum(@PathVariable(value = "id")Long id,@RequestHeader("Authorization")String authorization) throws ExecutionException, InterruptedException {
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        Future<Integer> futint = vacuumService.discharge(id,userId,null);
        Integer sw;
        while (true){
            if(futint.isDone()){
                sw = futint.get();
                break;
            }
            Thread.sleep(500);
        }
        switch (sw){
            case ERRORS.NOERROR:{
                return new ResponseEntity<>(HttpStatus.OK);
            }
            case ERRORS.NOTFOUND:{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            case ERRORS.OCCUPIED:{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            case ERRORS.ALREADYDISCHARGING:{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        }
        return null;
    }

    @GetMapping("/start/{id}/{time}")
    @jwt(permisions = ROLES.CAN_START_VACUUM)
    public ResponseEntity<String> startVacuum(@PathVariable(value = "id")Long id,@PathVariable(value = "time")Long time,@RequestHeader("Authorization")String authorization) throws ExecutionException, InterruptedException {
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        Future<Integer> futint = vacuumService.start(id,userId,time);
        Integer sw;
        while (true){
            if(futint.isDone()){
                sw = futint.get();
                break;
            }
            Thread.sleep(500);
        }
        switch (sw){
            case ERRORS.NOERROR:{
                return new ResponseEntity<>(HttpStatus.OK);
            }
            case ERRORS.NOTFOUND:{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            case ERRORS.OCCUPIED:{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            case ERRORS.ALREADYON:{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        }
        return null;
    }

    @GetMapping("/stop/{id}/{time}")
    @jwt(permisions = ROLES.CAN_STOP_VACUUM)
    public ResponseEntity<String> stopVacuum(@PathVariable(value = "id")Long id,@PathVariable(value = "time")Long time,@RequestHeader("Authorization")String authorization) throws ExecutionException, InterruptedException {
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        Future<Integer> futint = vacuumService.stop(id,userId,time);
        Integer sw;
        while (true){
            if(futint.isDone()){
                sw = futint.get();
                break;
            }
            Thread.sleep(500);
        }
        switch (sw){
            case ERRORS.NOERROR:{
                return new ResponseEntity<>(HttpStatus.OK);
            }
            case ERRORS.NOTFOUND:{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            case ERRORS.OCCUPIED:{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            case ERRORS.ALREADYOFF:{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        }
        return null;
    }

    @GetMapping("/discharge/{id}/{time}")
    @jwt(permisions = ROLES.CAN_DISCHARGE_VACUUM)
    public ResponseEntity<String> dischargeVacuum(@PathVariable(value = "id")Long id,@PathVariable(value = "time")Long time,@RequestHeader("Authorization")String authorization) throws ExecutionException, InterruptedException {
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        Future<Integer> futint = vacuumService.discharge(id,userId,time);
        Integer sw;
        while (true){
            if(futint.isDone()){
                sw = futint.get();
                break;
            }
            Thread.sleep(500);
        }
        switch (sw){
            case ERRORS.NOERROR:{
                return new ResponseEntity<>(HttpStatus.OK);
            }
            case ERRORS.NOTFOUND:{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            case ERRORS.OCCUPIED:{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            case ERRORS.ALREADYDISCHARGING:{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        }
        return null;
    }
    @GetMapping("/poke/{id}")
    public ResponseEntity<String> pokeVacuum(@PathVariable(value = "id")Long id) throws ExecutionException, InterruptedException {
        Future<Integer> futint = vacuumService.poke(id);
        Integer sw;
        while (true){
            if(futint.isDone()){
                sw = futint.get();
                break;
            }
            Thread.sleep(500);
        }
        switch (sw){
            case ERRORS.STILLUPDATING:{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
            case ERRORS.UPDATED:{
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return null;
    }

    @PutMapping("/add")
    @jwt(permisions = ROLES.CAN_ADD_VACUUM)
    public ResponseEntity<String> registerVacuum(@RequestBody @Valid NewVacuumDTO newVacuumDTO, @RequestHeader("Authorization")String authorization){
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        vacuumService.addVacuum(newVacuumDTO, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/remove/{id}")
    @jwt(permisions = ROLES.CAN_REMOVE_VACUUM)
    public ResponseEntity<String> removeVacuum(@PathVariable(value = "id")Long id,@RequestHeader("Authorization")String authorization) throws ExecutionException, InterruptedException {
        Long userId = tokenService.parseToken(authorization.split(" ")[1]).get("id",Long.class);
        Future<Integer> futint = vacuumService.remove(id,userId);
        Integer sw;
        while (true){
            if(futint.isDone()){
                sw = futint.get();
                break;
            }
            Thread.sleep(500);
        }
        switch (sw){
            case ERRORS.NOTFOUND:{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            case ERRORS.NOERROR:{
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return null;
    }
}
