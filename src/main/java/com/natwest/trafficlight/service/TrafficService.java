package com.natwest.trafficlight.service;

import com.natwest.trafficlight.model.*;
import com.natwest.trafficlight.repository.TrafficRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TrafficService {
    private Map<Direction,LightColour> lights = new EnumMap<>(Direction.class);

    private boolean paused = false;

    private final TrafficRepository trafficRepository;

    private final ReentrantLock lock = new ReentrantLock();
    public TrafficService(TrafficRepository trafficRepository){
        this.trafficRepository = trafficRepository;
        for (Direction direction : Direction.values()) {
            lights.put(direction, LightColour.RED);
        }
        trafficRepository.addEvent(new TrafficEvent(EventType.SYSTEM_STARTED));
    }

    public void changeLight(Direction direction, LightColour lightColour){
        lock.lock();
        try {
            if (paused) {
                throw new IllegalStateException("System is paused.");
            }
            if (direction == null || lightColour == null) {
                throw new IllegalArgumentException("Invalid Input.");
            }
            if (lightColour == LightColour.GREEN) {
                lights.replaceAll((d, v) -> LightColour.RED);
            }
            if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                lights.put(Direction.NORTH, lightColour);
                lights.put(Direction.SOUTH, lightColour);
            } else if (direction == Direction.EAST || direction == Direction.WEST) {
                lights.put(Direction.EAST, lightColour);
                lights.put(Direction.WEST, lightColour);
            }
            trafficRepository.addEvent(new TrafficEvent(EventType.LIGHT_CHANGE,new EnumMap<>(lights)));
        } finally {
            lock.unlock();
        }
    }
    public TrafficStatus getStatus(){
        return new TrafficStatus(lights);
    }

    public void pause(){
        paused = true;
        trafficRepository.addEvent(new TrafficEvent(EventType.SYSTEM_PAUSED));
    }

    public void resume(){
        paused = false;
        trafficRepository.addEvent(new TrafficEvent(EventType.SYSTEM_RESUMED));
    }

    public List<TrafficEvent> getAllEvents() {
      return trafficRepository.getAllEvents();
    }
}
