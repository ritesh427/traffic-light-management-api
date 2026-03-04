package com.natwest.trafficlight.model;

import java.util.Map;

public class TrafficStatus {
    private Map<Direction,LightColour> lights;

    public TrafficStatus(Map<Direction, LightColour> lights) {
        this.lights = lights;
    }
    public Map<Direction, LightColour> getLights() {
        return lights;
    }
}
