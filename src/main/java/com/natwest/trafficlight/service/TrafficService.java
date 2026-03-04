package com.natwest.trafficlight.service;

import com.natwest.trafficlight.model.Direction;
import com.natwest.trafficlight.model.LightColour;
import com.natwest.trafficlight.model.TrafficEvent;
import com.natwest.trafficlight.model.TrafficStatus;
import com.natwest.trafficlight.repository.TrafficRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TrafficService {
    private Map<Direction,LightColour> lights = new HashMap<>();

    private boolean paused = false;

    private final TrafficRepository trafficRepository;

    public TrafficService(TrafficRepository trafficRepository){
        this.trafficRepository = trafficRepository;
        for (Direction direction : Direction.values()) {
            lights.put(direction, LightColour.RED);
        }
    }

    public void print(){
        System.out.println(lights);
    }

    public void changeLight(Direction direction, LightColour lightColour){
        if (paused){
            System.out.println("System is paused.");
            return;
        }
        if (direction == null || lightColour == null) {
            System.out.println("Invalid input");
            return;
        }
        if (lightColour == LightColour.GREEN){
            lights.replaceAll((d, v) -> LightColour.RED);
        }
        if (direction == Direction.NORTH || direction == Direction.SOUTH){
            lights.put(Direction.NORTH,lightColour);
            lights.put(Direction.SOUTH,lightColour);
        }
        else if (direction == Direction.EAST || direction == Direction.WEST){
            lights.put(Direction.EAST,lightColour);
            lights.put(Direction.WEST,lightColour);
        }
        trafficRepository.addEvent(new TrafficEvent(direction,lightColour));
    }
    public TrafficStatus getStatus(){
        return new TrafficStatus(lights);
    }

    public void pause(){
        paused = true;
    }

    public void resume(){
        paused = false;
    }

}
