package rs.raf.vacuumbackend.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.raf.vacuumbackend.dto.NewVacuumDTO;
import rs.raf.vacuumbackend.model.Err;
import rs.raf.vacuumbackend.model.Vacuum;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;


public interface VacuumService {
    Future<List<Vacuum>> search(String name, List<Integer> status, Date dateFrom, Date dateTo,Long addedBy);
    Future<Integer> start(Long id,Long addedBy,Long time);
    Future<Integer> stop(Long id,Long addedBy,Long time);
    Future<Integer> discharge(Long id,Long addedBy,Long time);
    Future<Integer> addVacuum(NewVacuumDTO newVacuumDTO,Long userId);
    Future<Integer> remove(Long id,Long addedBy);
    Future<Integer> poke(Long id);
    Future<List<Vacuum>> findAllByUser(Long userId);
    Future<List<Err>> findAllErrorsByUser(Long userId);
    void update(Vacuum vacuum, Long userId) throws InterruptedException;

    @Async
    @Scheduled(fixedRate = 1000L)
    void scheduledThingy();
}
