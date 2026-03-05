package com.natwest.trafficlight.controller;

import com.natwest.trafficlight.model.Direction;
import com.natwest.trafficlight.model.LightColour;
import com.natwest.trafficlight.model.TrafficEvent;
import com.natwest.trafficlight.model.TrafficStatus;
import com.natwest.trafficlight.service.TrafficService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traffic")
public class TrafficController {

    private final TrafficService trafficService;

    public TrafficController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @PostMapping("/change")
    public ResponseEntity<String> changeLight(@RequestParam Direction direction, @RequestParam LightColour lightColour) {
        trafficService.changeLight(direction, lightColour);
        return ResponseEntity.ok("Light updated");
    }

    @GetMapping("/status")
    public TrafficStatus getStatus() {
        return trafficService.getStatus();
    }

    @PostMapping("/pause")
    public ResponseEntity<String> getPause() {
        trafficService.pause();
        return ResponseEntity.ok("System is Paused.");
    }

    @PostMapping("/resume")
    public ResponseEntity<String> getResume() {
        trafficService.resume();
        return ResponseEntity.ok("System is Resumed.");
    }

    @GetMapping("/history")
    public List<TrafficEvent> getHistory() {
        return trafficService.getAllEvents();
    }

    @PostMapping("/emergency")
    public ResponseEntity<String> emergencyMode(@RequestParam Direction direction){
        trafficService.emergencyMode(direction);
        return ResponseEntity.ok("Emergency mode activated for : " + direction);
    }
}
