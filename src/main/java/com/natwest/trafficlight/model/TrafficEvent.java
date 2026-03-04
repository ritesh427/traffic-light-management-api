package com.natwest.trafficlight.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TrafficEvent {

    private final Direction direction;
    private final LightColour lightColour;
    private final LocalDateTime time;

    public TrafficEvent(Direction direction, LightColour lightColour) {
        this.direction = direction;
        this.lightColour = lightColour;
        this.time = LocalDateTime.now();
    }
}
