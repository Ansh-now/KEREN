# KEREN

**Key Engine for Reasoning & Execution Node**

Distributed AI execution and orchestration system.

## V0.6 — Android Control App

Native Android app (Kotlin + Jetpack Compose) that acts as the **KEREN Control Center** on mobile.

### Features (V0.6)
- Dark futuristic Control Dashboard
- Real-time device status & heartbeat
- Task queue / execution / history
- Live Terminal streaming
- Nervous System visualization
- Structured logs
- Send commands from phone → execute on PC/SBC nodes

### Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + Clean Architecture
- **Real-time**: WebSocket (OkHttp)
- **Networking**: Retrofit + OkHttp
- **DI**: Hilt
- **Min SDK**: 26

### Structure
```
app/
├── src/main/java/com/keren/control/
│   ├── ui/                 # Screens & Composables
│   ├── data/               # Repository, API, WebSocket
│   ├── domain/             # Models, UseCases
│   ├── di/                 # Hilt modules
│   └── MainActivity.kt
└── ...
```

### Current Status
- Project scaffolding started
- Dark theme + navigation ready
- Core screens structure in progress

---
Made for KEREN nervous system.
