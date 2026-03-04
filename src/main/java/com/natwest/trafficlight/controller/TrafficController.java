package com.natwest.trafficlight.controller;

import com.natwest.trafficlight.model.Direction;
import com.natwest.trafficlight.model.LightColour;
import com.natwest.trafficlight.model.TrafficStatus;
import com.natwest.trafficlight.repository.TrafficRepository;
import com.natwest.trafficlight.service.TrafficService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/traffic")
public class TrafficController {

    private final TrafficService trafficService;
    private final TrafficRepository trafficRepository;

    public TrafficController(TrafficService trafficService,TrafficRepository trafficRepository){
        this.trafficService = trafficService;
        this.trafficRepository = trafficRepository;
    }

    @PostMapping("/change")
    public String changeLight(@RequestParam Direction direction, @RequestParam LightColour lightColour){
        trafficService.changeLight(direction,lightColour);
        return "Light colour is set for given direction.";
    }

    @GetMapping("/test")
    public void test(){
        trafficService.print();
    }

    @GetMapping("/status")
    public TrafficStatus getStatus(){
        return trafficService.getStatus();
    }

    @GetMapping("/pause")
    public String getPause(){
        trafficService.pause();
        return "System is Paused.";
    }

    @GetMapping("/resume")
    public String getResume(){
        trafficService.resume();
        return "System is Resumed.";
    }

    @GetMapping("/history")
    public Object getHistory() {
        return trafficRepository.getAllEvents();
    }
}
