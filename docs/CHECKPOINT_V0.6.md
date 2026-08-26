# KEREN V0.6 — Checkpoint Log

**Repo:** https://github.com/Ansh-now/KEREN  
**Last updated:** 2026-08-26  
**Principle:** REAL DATA → REAL EVENTS → REAL EXECUTION → REAL VISUALIZATION

---

## CHECKPOINT 0 — Initial Audit (2026-08-26)

### Repository structure

```
KEREN/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/keren/control/
│       │   ├── KerenApp.kt
│       │   ├── MainActivity.kt
│       │   ├── domain/model/NervousModels.kt
│       │   └── ui/
│       │       ├── navigation/KerenNavHost.kt
│       │       ├── screens/
│       │       │   ├── overview/OverviewScreen.kt
│       │       │   ├── devices/DevicesScreen.kt
│       │       │   ├── tasks/TasksScreen.kt
│       │       │   ├── terminal/TerminalScreen.kt
│       │       │   ├── nervous/NervousSystemScreen.kt
│       │       │   ├── logs/LogsScreen.kt
│       │       │   └── settings/SettingsScreen.kt
│       │       └── theme/
│       └── res/
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

### What exists (good)

- Dark theme + monospace typography
- Bottom navigation (Overview, Devices, Tasks, Terminal, Nervous)
- Hilt Application + MainActivity
- Compose Material 3 + Navigation
- Nervous System Canvas foundation (nodes, edges, packets, pulse, zoom/pan)
- Domain models for Nervous graph (`KerenNode`, `KerenEdge`, `LivePacket`, `NervousEvent`, `NervousSnapshot`)
- Networking dependencies already in Gradle (OkHttp, Retrofit, Gson)
- Coroutines + ViewModel Compose deps present

### What is MISSING / DEMO

| Area | Status | Problem |
|------|--------|---------|
| `data/` layer | **Missing** | No API, no WebSocket, no DTOs, no repositories |
| ViewModels | **Missing** | Screens hold local/hardcoded state |
| ConnectionManager | **Missing** | No connect/reconnect/resync |
| DeviceRepository | **Missing** | DevicesScreen has hardcoded `DeviceUi` list |
| TaskRepository | **Missing** | TasksScreen has hardcoded queued/executed lists |
| Event bus client | **Missing** | No WebSocket event parser |
| Terminal submit | **Demo** | Hardcoded output lines, no POST /tasks |
| Overview | **Demo** | Static status numbers + static activity lines |
| Logs | **Demo** | Static log strings |
| Nervous System data | **Demo** | `createDemoSnapshot()` + continuous infinite packet animation |
| Settings | **Partial** | Hardcoded `ws://192.168.1.10:8080` |
| Auth | **Missing** | No token / pairing |
| KEREN Core backend | **Not in this repo** | Android is client-only |

### Demo / static data locations (must be removed)

1. `DevicesScreen.kt` — hardcoded `listOf(DeviceUi(...))`
2. `TasksScreen.kt` — hardcoded queued + executed lists
3. `OverviewScreen.kt` — static status pairs + static activity lines
4. `TerminalScreen.kt` — hardcoded output `mutableStateListOf(...)`
5. `LogsScreen.kt` — hardcoded log lines
6. `NervousSystemScreen.kt` — `createDemoSnapshot()` + infinite packet loop
7. `SettingsScreen.kt` — hardcoded Core address

### Backend dependency

**KEREN Core is NOT present in this repository.**

Android app must treat Core as external authority.

Required before claiming integration complete:

- Define REST + WebSocket protocol (v0.6)
- Implement typed clients + repositories
- Implement ConnectionManager (connect / reconnect / resync)
- Wire all screens to StateFlows from repositories
- Document required Core endpoints

---

## Required Core API Contract (v0.6)

### REST

```
GET    /v0.6/health
GET    /v0.6/devices
GET    /v0.6/devices/{id}
POST   /v0.6/devices/register
DELETE /v0.6/devices/{id}

POST   /v0.6/tasks
GET    /v0.6/tasks
GET    /v0.6/tasks/{id}
POST   /v0.6/tasks/{id}/cancel
POST   /v0.6/tasks/{id}/retry

GET    /v0.6/events?since={cursor}
```

### WebSocket

```
WS /v0.6/ws
```

Event envelope:

```json
{
  "protocol_version": "0.6",
  "event_id": "evt_...",
  "event": "task.started",
  "timestamp": "2026-08-26T12:30:00Z",
  "task_id": "task_...",
  "source_node_id": "phone",
  "target_node_id": "pc-01",
  "payload": {}
}
```

### Key events

- `device.connected` / `device.disconnected` / `device.heartbeat` / `device.telemetry`
- `task.created` / `task.queued` / `task.planning` / `task.routed`
- `task.dispatched` / `task.started` / `task.stdout` / `task.stderr`
- `task.completed` / `task.failed` / `task.cancelled` / `task.timeout`

---

## Implementation Priority

### P0 (must work)
1. Core connection config (Settings)
2. REST client + DTOs
3. WebSocket client + event parser
4. ConnectionManager (state + reconnect + backoff)
5. DeviceRepository + TaskRepository + EventRepository
6. Central state (StateFlow)
7. Device sync + Task sync
8. Real Terminal task submission

### P1
9. stdout/stderr streaming into Terminal
10. Live Overview counters from same state
11. Real queue + history
12. Add Device flow
13. Reconnect → full resync

### P2
14. Event-driven Nervous System (no fake packets)
15. Task trace mode
16. Structured logs from EventRepository
17. Auth / pairing improvements

---

## Checkpoint history

| ID | Date | Summary |
|----|------|---------|
| CP0 | 2026-08-26 | Full audit completed. Demo data mapped. Core API contract defined. Architecture plan locked. |
| CP1 | TBD | data/ layer + ConnectionManager + typed models |
| CP2 | TBD | Repositories + ViewModels wired to UI |
| CP3 | TBD | Terminal real submit + event stream |
| CP4 | TBD | Nervous System driven by real events only |

---

## Rules (do not violate)

1. No fake CPU/RAM/status/telemetry in production paths.
2. No continuous random packet animation.
3. No hardcoded production Core IPs.
4. UI must show DISCONNECTED when Core is unreachable.
5. After reconnect → resync devices + tasks + resume WS.
6. Single source of truth via repositories → StateFlow → UI.
7. Preserve existing dark theme + navigation + Nervous visual foundation.
