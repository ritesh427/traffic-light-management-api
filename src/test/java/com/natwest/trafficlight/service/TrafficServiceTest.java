package com.natwest.trafficlight.service;

import com.natwest.trafficlight.model.Direction;
import com.natwest.trafficlight.model.LightColour;
import com.natwest.trafficlight.model.TrafficStatus;
import com.natwest.trafficlight.repository.TrafficRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrafficServiceTest {

    private TrafficService trafficService;
    private TrafficRepository trafficRepository;

    @BeforeEach
    void setup() {
        trafficRepository = new TrafficRepository();
        trafficService = new TrafficService(trafficRepository);
    }

    @Test
    void testInitializeAllLightsAsRed() {
        TrafficStatus status = trafficService.getStatus();
        assertEquals(LightColour.RED, status.getLights().get(Direction.NORTH));
        assertEquals(LightColour.RED, status.getLights().get(Direction.SOUTH));
        assertEquals(LightColour.RED, status.getLights().get(Direction.EAST));
        assertEquals(LightColour.RED, status.getLights().get(Direction.WEST));
    }

    @Test
    void testChangeNorthSouthToGreen() {
        trafficService.changeLight(Direction.NORTH, LightColour.GREEN);
        TrafficStatus status = trafficService.getStatus();
        assertEquals(LightColour.GREEN, status.getLights().get(Direction.NORTH));
        assertEquals(LightColour.GREEN, status.getLights().get(Direction.SOUTH));
        assertEquals(LightColour.RED, status.getLights().get(Direction.EAST));
        assertEquals(LightColour.RED, status.getLights().get(Direction.WEST));
    }

    @Test
    void testChangeEastWestToGreen() {
        trafficService.changeLight(Direction.EAST, LightColour.GREEN);
        TrafficStatus status = trafficService.getStatus();
        assertEquals(LightColour.GREEN, status.getLights().get(Direction.EAST));
        assertEquals(LightColour.GREEN, status.getLights().get(Direction.WEST));
        assertEquals(LightColour.RED, status.getLights().get(Direction.NORTH));
        assertEquals(LightColour.RED, status.getLights().get(Direction.SOUTH));
    }

    @Test
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> trafficService.changeLight(null, null));
    }

    @Test
    void testPauseSystem() {
        trafficService.pause();
        assertThrows(IllegalStateException.class, () -> trafficService.changeLight(Direction.NORTH, LightColour.GREEN));
    }

    @Test
    void testResumeSystem() {
        trafficService.pause();
        trafficService.resume();
        assertDoesNotThrow(() -> trafficService.changeLight(Direction.NORTH, LightColour.GREEN));
    }

    @Test
    void testRecordEventHistory() {
        trafficService.changeLight(Direction.NORTH, LightColour.GREEN);
        assertFalse(trafficService.getAllEvents().isEmpty());
    }
}