# KEREN

**Key Engine for Reasoning & Execution Node**

Distributed AI execution and orchestration system.

## V0.6 — Android Control App

Native Android app (Kotlin + Jetpack Compose) that acts as the **KEREN Control Center** on mobile.

### Signature Feature — NERVOUS SYSTEM

Live visualization of task flow through the entire system.

- Nodes: USER → KEREN CORE → PLANNER / ROUTER → DEVICE → WORKER → APP
- Animated energy packets travel along active edges
- Node states: Idle / Receiving / Processing / Sending / Error / Offline
- Two-way flow (command + result)
- Event stream at bottom (real events only)
- Pinch-to-zoom + pan
- Designed to be driven by KEREN Event Bus (no fake animation)

### Other Features
- Dark futuristic Control Dashboard
- Real-time device status & heartbeat
- Task queue / execution / history
- Live Terminal streaming
- Structured logs
- Send commands from phone → execute on PC/SBC nodes

### Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3 + Canvas
- **Architecture**: MVVM + Clean Architecture
- **Real-time**: WebSocket (OkHttp)
- **DI**: Hilt
- **Min SDK**: 26

### Structure
```
app/
├── src/main/java/com/keren/control/
│   ├── domain/model/       # NervousModels, Task, Device...
│   ├── ui/
│   │   ├── screens/nervous/  # Live graph + packets
│   │   ├── screens/overview/
│   │   ├── screens/devices/
│   │   ├── screens/tasks/
│   │   ├── screens/terminal/
│   │   └── navigation/
│   ├── data/
│   └── MainActivity.kt
└── ...
```

### Current Status
- Project scaffolding complete
- Dark theme + bottom navigation ready
- **Nervous System Live View** (Canvas + animated packets) implemented
- Domain models for nodes, edges, packets, events ready
- Next: WebSocket + real Event Bus binding

---
Made for KEREN nervous system.
