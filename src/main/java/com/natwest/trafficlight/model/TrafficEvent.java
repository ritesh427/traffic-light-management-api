package com.natwest.trafficlight.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class TrafficEvent {

    private final EventType type;
    private final Map<Direction, LightColour> state;
    private final LocalDateTime time;

    public TrafficEvent(EventType type, Map<Direction, LightColour> state) {
        this.type = type;
        this.time = LocalDateTime.now();
        this.state = state;
    }

    public TrafficEvent(EventType type) {
        this.type = type;
        this.time = LocalDateTime.now();
        this.state = null;
    }
}
