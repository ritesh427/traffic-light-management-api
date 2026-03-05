# Traffic Light Management API

## Features

* Manages traffic lights for four directions: NORTH, SOUTH, EAST, WEST.
* Ensures conflicting directions cannot be green at the same time.
* Allows manual control of traffic lights using REST APIs.
* Supports pause and resume functionality for the system.
* Maintains an event history including light changes and system events.
* Stores the state of all lights at the time of each event.
* Uses a lock in the service layer to handle concurrent requests safely.

---

## APIs to Test

### Change Traffic Light

```http
POST /traffic/change?direction=NORTH&lightColour=GREEN
```

### Get Current Status

```http
GET /traffic/status
```

### Pause System

```http
POST /traffic/pause
```

### Resume System

```http
POST /traffic/resume
```

### Get Event History

```http
GET /traffic/history
```

---

## Possible Enhancement

In a real traffic system, signals usually change automatically after fixed intervals.
This project could be extended by adding a **scheduler** (for example using Spring’s `@Scheduled`) to automatically rotate traffic signals between **North/South and East/West directions** while still respecting pause and resume operations.

---
