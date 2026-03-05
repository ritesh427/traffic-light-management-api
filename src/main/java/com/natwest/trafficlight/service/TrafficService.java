package com.natwest.trafficlight.service;

import com.natwest.trafficlight.model.*;
import com.natwest.trafficlight.repository.TrafficRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class TrafficService {
    private final Map<Direction, LightColour> lights = new EnumMap<>(Direction.class);

    private boolean paused = false;

    private int currentPhase = 0;

    private final TrafficRepository trafficRepository;

    private final ReentrantLock lock = new ReentrantLock();

    public TrafficService(TrafficRepository trafficRepository) {
        log.info("Initializing traffic lights: setting ALL directions to RED on system startup");
        this.trafficRepository = trafficRepository;
        for (Direction direction : Direction.values()) {
            lights.put(direction, LightColour.RED);
        }
        trafficRepository.addEvent(new TrafficEvent(EventType.SYSTEM_STARTED));
    }

    public void changeLight(Direction direction, LightColour lightColour) {
        lock.lock();
        try {
            if (paused) {
                log.info("System is paused.");
                throw new IllegalStateException("System is paused.");
            }
            if (direction == null || lightColour == null) {
                log.warn("Invalid light change request: direction={}, colour={}", direction, lightColour);
                throw new IllegalArgumentException("Invalid Input.");
            }
            if (lightColour == LightColour.GREEN) {
                setAllRed();
            }
            if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                lights.put(Direction.NORTH, lightColour);
                lights.put(Direction.SOUTH, lightColour);
            } else if (direction == Direction.EAST || direction == Direction.WEST) {
                lights.put(Direction.EAST, lightColour);
                lights.put(Direction.WEST, lightColour);
            }
            log.info("Traffic light state change : direction={}, colour={}", direction, lightColour);
            trafficRepository.addEvent(new TrafficEvent(EventType.LIGHT_CHANGE, new EnumMap<>(lights)));
        } finally {
            lock.unlock();
        }
    }

    public TrafficStatus getStatus() {
        log.info("Checking Status...");
        return new TrafficStatus(lights);
    }

    public void pause() {
        paused = true;
        trafficRepository.addEvent(new TrafficEvent(EventType.SYSTEM_PAUSED));
        log.info("Traffic system paused.");
    }

    public void resume() {
        paused = false;
        trafficRepository.addEvent(new TrafficEvent(EventType.SYSTEM_RESUMED));
        log.info("Traffic system resumed.");
    }

    public List<TrafficEvent> getAllEvents() {
        log.info("Fetching all events.");
        return trafficRepository.getAllEvents();
    }

    public void emergencyMode(Direction direction){
        lock.lock();
        try {
            if (direction==null){
                throw new IllegalArgumentException("Invalid direction for emergency.");
            }
            setAllRed();
            if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                lights.put(Direction.NORTH, LightColour.GREEN);
                lights.put(Direction.SOUTH, LightColour.GREEN);
            } else {
                lights.put(Direction.EAST, LightColour.GREEN);
                lights.put(Direction.WEST, LightColour.GREEN);
            }
            trafficRepository.addEvent(new TrafficEvent(EventType.EMERGENCY_MODE, new EnumMap<>(lights)));
        } finally {
            lock.unlock();
        }
    }
    @Scheduled(fixedRate = 30000)
    public void schedule(){
        if (paused) {
            log.info("Scheduler paused - skipping cycle");
            return;
        };
        lock.lock();
        try{
            switch (currentPhase){
                case 0:
                    setNorthAndSouth(LightColour.GREEN);
                    log.info("Lights changed: North-South → GREEN");
                    break;
                case 1:
                    setNorthAndSouth(LightColour.YELLOW);
                    log.info("Lights changed: North-South → YELLOW");
                    break;
                case 2:
                    setEastAndWest(LightColour.GREEN);
                    log.info("Lights changed: East-West → GREEN");
                    break;
                case 3:
                    setEastAndWest(LightColour.YELLOW);
                    log.info("Lights changed: East-West → YELLOW");
                    break;
            }
            currentPhase = (currentPhase + 1) % 4;
            trafficRepository.addEvent(new TrafficEvent(EventType.LIGHT_CHANGE,new EnumMap<>(lights)));
        } finally {
            lock.unlock();
        }
    }

    private void setNorthAndSouth(LightColour colour) {
        setAllRed();
        lights.put(Direction.NORTH, colour);
        lights.put(Direction.SOUTH, colour);
    }
    private void setEastAndWest(LightColour colour) {
        setAllRed();
        lights.put(Direction.EAST, colour);
        lights.put(Direction.WEST, colour);
    }

    private void setAllRed(){
        lights.replaceAll((d,v)->LightColour.RED);
    }

}
