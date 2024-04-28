package rs.raf.vacuumbackend.service;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.raf.vacuumbackend.dto.NewVacuumDTO;
import rs.raf.vacuumbackend.model.Err;
import rs.raf.vacuumbackend.repository.ErrRepository;
import rs.raf.vacuumbackend.staticNumers.ERRORS;
import rs.raf.vacuumbackend.staticNumers.STATUSES;
import rs.raf.vacuumbackend.mapper.VacuumMapper;
import rs.raf.vacuumbackend.model.Vacuum;
import rs.raf.vacuumbackend.repository.VacuumRepository;

import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class VacuumServiceImplementation implements VacuumService{

    VacuumRepository vacuumRepository;
    VacuumMapper vacuumMapper;
    ErrRepository errRepository;
    @Autowired
    private ApplicationContext applicationContext;
    private VacuumService getSelfProxy() {
        return applicationContext.getBean(VacuumService.class);
    }

    public VacuumServiceImplementation(VacuumRepository vacuumRepository, VacuumMapper vacuumMapper, ErrRepository errRepository) {
        this.vacuumRepository = vacuumRepository;
        this.vacuumMapper = vacuumMapper;
        this.errRepository = errRepository;
    }

    @Async
    @Override
    public Future<List<Vacuum>> search(String name, List<Integer> status, Date dateFrom, Date dateTo,Long addedBy) {
        if(dateTo.getTime()==0)
            dateTo = null;
        if(dateFrom.getTime()==0)
            dateFrom = null;
        boolean hadPreviousCondition = false;
        List<Vacuum> vacuums = new ArrayList<>();
        if(name != null && !(name.trim().equals(""))){
            vacuums = vacuumRepository.findVacuumsByActiveAndNameAndAddedBy(true,name,addedBy);
            hadPreviousCondition = true;
        }
        if(status!=null && !(status.isEmpty())){
            if(vacuums.isEmpty() && !hadPreviousCondition){
                for(Integer stat:status){
                    vacuums.addAll(vacuumRepository.findVacuumsByActiveAndStatusAndAddedBy(true,stat,addedBy));
                }
            }else {
                List<Vacuum> vacuums1 = new ArrayList<>();
                for(Integer stat:status){
                    vacuums1.addAll(vacuumRepository.findVacuumsByActiveAndStatusAndAddedBy(true,stat,addedBy));
                }
                vacuums = vacuums.stream()
                        .filter(vacuum -> vacuums1.stream().anyMatch(v -> v.getVacuumId() == vacuum.getVacuumId()))
                        .collect(Collectors.toList());
            }
            hadPreviousCondition = true;
        }
        if(dateFrom != null){

            if(vacuums.isEmpty() && !hadPreviousCondition){
                vacuums = vacuumRepository.findVacuumsByActiveAndDateCreatedIsAfterAndAddedBy(true,dateFrom,addedBy);
            }else {
                List<Vacuum> vacuums1 = new ArrayList<>();
                vacuums1.addAll(vacuumRepository.findVacuumsByActiveAndDateCreatedIsAfterAndAddedBy(true, dateFrom,addedBy));
                vacuums = vacuums.stream()
                        .filter(vacuum -> vacuums1.stream().anyMatch(v -> v.getVacuumId() == vacuum.getVacuumId()))
                        .collect(Collectors.toList());
            }
            hadPreviousCondition = true;
        }
        if(dateTo != null){
            if(vacuums.isEmpty() && !hadPreviousCondition){
                vacuums = vacuumRepository.findVacuumsByActiveAndDateCreatedIsBeforeAndAddedBy(true,dateTo,addedBy);
            }else {
                List<Vacuum> vacuums1 = new ArrayList<>();
                vacuums1.addAll(vacuumRepository.findVacuumsByActiveAndDateCreatedIsBeforeAndAddedBy(true, dateTo,addedBy));
                vacuums = vacuums.stream()
                        .filter(vacuum -> vacuums1.stream().anyMatch(v -> v.getVacuumId() == vacuum.getVacuumId()))
                        .collect(Collectors.toList());}
            hadPreviousCondition = true;
        }
        if(vacuums.isEmpty() && !hadPreviousCondition) {
            vacuums = vacuumRepository.findVacuumsByActiveAndAddedBy(true,addedBy);
        }
        System.out.println(vacuums);
            return new AsyncResult<>(vacuums);
    }

    @Async
    @Override
    public Future<Integer> start(Long id,Long addedBy,Long time) {
        Optional<Vacuum> vac = vacuumRepository.findVacuumsByActiveAndVacuumIdAndAddedBy(true,id,addedBy);
        Vacuum vacuum;
        if(vac.isPresent()){
            vacuum = vac.get();
        }else return new AsyncResult<>(ERRORS.NOTFOUND);
        if(vacuum.getNextStatus()!= null){
            errRepository.save(new Err(null,vacuum.getAddedBy(), vacuum.getVacuumId(),ERRORS.OCCUPIED,System.currentTimeMillis()));
            return new AsyncResult<>(ERRORS.OCCUPIED);
        }
        if(vacuum.getStatus() != STATUSES.STOP){
            errRepository.save(new Err(null,vacuum.getAddedBy(), vacuum.getVacuumId(),ERRORS.ALREADYON,System.currentTimeMillis()));
            return new AsyncResult<>(ERRORS.ALREADYON);
        }
        vacuum.setNextStatus(STATUSES.START);
        vacuum.setNumberOfCycles(vacuum.getNumberOfCycles()+1);
        if(time == null || time<System.currentTimeMillis()) {
            vacuum.setNextStatusExpectedTime(null);
            vacuumRepository.save(vacuum);
            try {
                getSelfProxy().update(vacuum, addedBy);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else {
            vacuum.setNextStatusExpectedTime(time);
            vacuumRepository.save(vacuum);
        }
        return new AsyncResult<>(ERRORS.NOERROR);
    }

    @Async
    @Override
    public Future<Integer> stop(Long id,Long addedBy,Long time) {
        Optional<Vacuum> vac = vacuumRepository.findVacuumsByActiveAndVacuumIdAndAddedBy(true,id,addedBy);
        Vacuum vacuum;
        if(vac.isPresent()){
            vacuum = vac.get();
        }else return new AsyncResult<>(ERRORS.NOTFOUND);
        if(vacuum.getNextStatus()!= null) {
            errRepository.save(new Err(null,vacuum.getAddedBy(), vacuum.getVacuumId(),ERRORS.OCCUPIED,System.currentTimeMillis()));
            return new AsyncResult<>(ERRORS.OCCUPIED);
        }
        if(vacuum.getStatus() == STATUSES.STOP) {
            errRepository.save(new Err(null,vacuum.getAddedBy(), vacuum.getVacuumId(),ERRORS.ALREADYOFF,System.currentTimeMillis()));
            return new AsyncResult<>(ERRORS.ALREADYOFF);
        }
        vacuum.setNextStatus(STATUSES.STOP);
        if(time == null || time<System.currentTimeMillis()) {
            vacuum.setNextStatusExpectedTime(null);
            vacuumRepository.save(vacuum);
            try {
                getSelfProxy().update(vacuum, addedBy);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else {
            vacuum.setNextStatusExpectedTime(time);
            vacuumRepository.save(vacuum);
        }
        return new AsyncResult<>(ERRORS.NOERROR);
    }

    @Async
    @Override
    public Future<Integer> discharge(Long id,Long addedBy,Long time){
        Optional<Vacuum> vac = vacuumRepository.findVacuumsByActiveAndVacuumIdAndAddedBy(true,id,addedBy);
        Vacuum vacuum;
        if(vac.isPresent()){
            vacuum = vac.get();
        }else return new AsyncResult<>(ERRORS.NOTFOUND);
        if(vacuum.getNextStatus()!= null){
            errRepository.save(new Err(null,vacuum.getAddedBy(), vacuum.getVacuumId(),ERRORS.OCCUPIED,System.currentTimeMillis()));
            return new AsyncResult<>(ERRORS.OCCUPIED);
        }
        if(vacuum.getStatus() == STATUSES.DISCHARGE) {
            errRepository.save(new Err(null,vacuum.getAddedBy(), vacuum.getVacuumId(),ERRORS.ALREADYDISCHARGING,System.currentTimeMillis()));
            return new AsyncResult<>(ERRORS.ALREADYDISCHARGING);
        }
        vacuum.setNextStatus(STATUSES.DISCHARGE);
        vacuum.setNumberOfCycles(0);
        if(time == null || time<System.currentTimeMillis()) {
            vacuum.setNextStatusExpectedTime(null);
            vacuumRepository.save(vacuum);
            try {
                getSelfProxy().update(vacuum, addedBy);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else {
            vacuum.setNextStatusExpectedTime(time);
            vacuumRepository.save(vacuum);
        }
        return new AsyncResult<>(ERRORS.NOERROR);
    }

    @Async
    @Override
    public Future<Integer> addVacuum(NewVacuumDTO newVacuumDTO,Long userId) {
        vacuumRepository.save(vacuumMapper.NewVacuumDTOToVacuum(newVacuumDTO,userId));
        return new AsyncResult<>(ERRORS.NOERROR);
    }

    @Async
    @Override
    public Future<Integer> remove(Long id,Long addedBy) {
        Optional<Vacuum> vac = vacuumRepository.findVacuumsByActiveAndVacuumIdAndAddedBy(true,id,addedBy);
        Vacuum vacuum;
        if(vac.isPresent()){
            vacuum = vac.get();
        }else return new AsyncResult<>(ERRORS.NOTFOUND);
        vacuum.setActive(false);
        vacuumRepository.save(vacuum);
        return new AsyncResult<>(ERRORS.NOERROR);
    }

    @Async
    @Override
    public Future<Integer> poke(Long id) {Optional<Vacuum> vac = vacuumRepository.findVacuumsByActiveAndVacuumId(true,id);
        Vacuum vacuum;
        if(vac.isPresent()){
            vacuum = vac.get();
        }else return new AsyncResult<>(ERRORS.NOTFOUND);
        if(vacuum.getNextStatus()!=null)
            return new AsyncResult<>(ERRORS.STILLUPDATING);
        return new AsyncResult<>(ERRORS.UPDATED);
    }

    @Async
    @Override
    public Future<List<Vacuum>> findAllByUser(Long userId) {
        return new AsyncResult<>(vacuumRepository.findVacuumsByActiveAndAddedBy(true,userId));
    }

    @Async
    @Override
    public Future<List<Err>> findAllErrorsByUser(Long userId) {
        return new AsyncResult<>(errRepository.findErrsByUserId(userId));
    }

    @Async
    @Override
    public void update(Vacuum vacuum, Long userId) throws InterruptedException {
        Thread.sleep(15000);
        if(vacuum.getNextStatus()==null)
            return;
        vacuum.setStatus(vacuum.getNextStatus());
        vacuum.setNextStatus(null);
        vacuum.setNextStatusExpectedTime(null);
        vacuumRepository.save(vacuum);
        if(vacuum.getStatus()== STATUSES.STOP && vacuum.getNumberOfCycles() == 3){
            discharge(vacuum.getVacuumId(),userId,null);
        }
        if(vacuum.getStatus()==STATUSES.DISCHARGE){
            stop(vacuum.getVacuumId(),userId,null);
        }
    }

    @Async
    @Scheduled(fixedRate = 1000L)
    @Override
    public void scheduledThingy(){
        List<Vacuum> vacuums = vacuumRepository.findVacuumsByActiveTrueAndNextStatusExpectedTimeNotNull();
        if(vacuums.isEmpty())
            return;
        for(Vacuum vacuum:vacuums){
            if(vacuum.getNextStatusExpectedTime()<System.currentTimeMillis()){
                switch (vacuum.getNextStatus()){
                    case STATUSES.START:{
                        start(vacuum.getVacuumId(),vacuum.getAddedBy(),null);
                        break;
                    }
                    case STATUSES.STOP:{
                        stop(vacuum.getVacuumId(),vacuum.getAddedBy(),null);
                        break;
                    }
                    case STATUSES.DISCHARGE:{
                        discharge(vacuum.getVacuumId(),vacuum.getAddedBy(),null);
                        break;
                    }
                }
                try {
                    getSelfProxy().update(vacuum,vacuum.getAddedBy());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
