# Traffic Light Management API

## Overview

This project implements a simple traffic light controller for a four-way intersection.
It manages traffic signals for **North, South, East, and West directions** while ensuring that conflicting directions are never green at the same time.

The application exposes REST APIs to manually control signals, pause or resume the system, trigger emergency priority, and view the history of traffic events. It also supports **automatic signal rotation and night mode scheduling**.

---

# How the Traffic System Works

The system uses a **phase-based traffic control model**.

Traffic signals rotate through the following phases:

| Phase | NORTH  | SOUTH  | EAST   | WEST   |
| ----- | ------ | ------ | ------ | ------ |
| 1     | GREEN  | GREEN  | RED    | RED    |
| 2     | YELLOW | YELLOW | RED    | RED    |
| 3     | RED    | RED    | GREEN  | GREEN  |
| 4     | RED    | RED    | YELLOW | YELLOW |

This ensures:

* **North and South move together**
* **East and West move together**
* Conflicting directions are never green simultaneously.

The phases rotate automatically using a **scheduler**.

---

# Features

## Traffic Control

* Manages traffic lights for **four directions**: NORTH, SOUTH, EAST, WEST.
* Ensures **conflicting directions cannot be green at the same time**.
* Allows **manual control of traffic lights** through REST APIs.

---

## Automatic Traffic Cycle

* A scheduler automatically rotates traffic signals between phases.
* Traffic lights switch between **North/South and East/West directions**.

---

## Pause and Resume

* The traffic system can be **paused and resumed manually**.
* When paused, traffic light changes and scheduler updates are blocked.

---

## Emergency Vehicle Priority

* Supports **emergency mode** for scenarios such as ambulances.
* The requested direction immediately becomes **GREEN** while other directions remain **RED**.

---

## Night Mode Automation

* The system automatically pauses traffic operations at **11 PM**.
* It automatically resumes operations at **5 AM**.

---

## Event History

The system records a **history of all traffic events**, including:

* traffic light changes
* system start
* pause and resume events
* emergency mode activation

Each event records the **complete state of all traffic lights** at that moment.

---

## Thread Safety

The service layer uses **ReentrantLock** to ensure that traffic light updates remain consistent when multiple API requests occur simultaneously.

---

## Global Exception Handling

The application uses **Spring’s `@RestControllerAdvice`** to handle exceptions globally and return consistent API responses.

Examples of handled exceptions:

* `IllegalArgumentException` → Invalid request input
* `IllegalStateException` → Invalid system state (for example when system is paused)

This ensures that APIs return **clean and consistent error responses** instead of raw stack traces.

---

# APIs to Test

## Change Traffic Light

Manually update traffic light state for a direction.

```http
POST /traffic/change?direction=NORTH&lightColour=GREEN
```

---

## Get Current Status

Returns the current state of all traffic lights.

```http
GET /traffic/status
```

Example response:

```json
{
  "lights": {
    "NORTH": "GREEN",
    "SOUTH": "GREEN",
    "EAST": "RED",
    "WEST": "RED"
  }
}
```

---

## Pause Traffic System

Stops traffic light operations.

```http
POST /traffic/pause
```

---

## Resume Traffic System

Resumes normal traffic operations.

```http
POST /traffic/resume
```

---

## Emergency Mode

Activates emergency priority for a direction.

```http
POST /traffic/emergency?direction=NORTH
```

---

## Get Event History

Returns a list of events recorded by the system.

```http
GET /traffic/history
```

Example response:

```json
[
  {
    "type": "LIGHT_CHANGE",
    "time": "2026-03-05T01:52:02",
    "state": {
      "NORTH": "GREEN",
      "SOUTH": "GREEN",
      "EAST": "RED",
      "WEST": "RED"
    }
  }
]
```

---

# Technologies Used

* Java
* Spring Boot
* Spring Web
* Spring Scheduler (`@Scheduled`)
* Lombok
* JUnit

---

# Possible Enhancements

This project can be extended with additional real-world traffic management capabilities:

* **Pedestrian crossing phase** for safe footpath crossing.
* **Adaptive traffic signals** based on traffic density or sensors.
* **Multiple intersection support** for managing city-wide traffic systems.
* **Traffic monitoring and analytics dashboards**.
* **Integration with real-time traffic sensors or cameras**.

---
