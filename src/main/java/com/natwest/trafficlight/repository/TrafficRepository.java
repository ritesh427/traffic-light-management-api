package com.natwest.trafficlight.repository;

import com.natwest.trafficlight.model.TrafficEvent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TrafficRepository {
    private final List<TrafficEvent> events = new ArrayList<>();

    public void addEvent(TrafficEvent event) {
        events.add(event);
    }

    public List<TrafficEvent> getAllEvents() {
        return events;
    }
}
