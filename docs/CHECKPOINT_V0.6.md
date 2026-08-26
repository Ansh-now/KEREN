# KEREN V0.6 — Checkpoint Log

**Repo:** https://github.com/Ansh-now/KEREN  
**Last updated:** 2026-08-26  
**Principle:** REAL DATA → REAL EVENTS → REAL EXECUTION → REAL VISUALIZATION

---

## CHECKPOINT 0 — Initial Audit (2026-08-26)

Full audit completed. Demo data locations mapped. Core is NOT in this repo. Android is client-only.

See previous commit history for full audit table.

---

## CHECKPOINT 1 — Data layer + Connection foundation (2026-08-26)

### Added

**Domain models**
- `Device` + `DeviceStatus` + `DeviceTelemetry`
- `Task` + `TaskStatus` + `CreateTaskRequest`
- `KerenEvent` + `KerenEventType` constants
- `ConnectionState` + `CoreConfig` + `ConnectionInfo`

**Repository interfaces**
- `DeviceRepository`
- `TaskRepository`
- `EventRepository`
- `ConnectionRepository`

**Data / remote**
- `KerenApi` (Retrofit v0.6 REST contract)
- DTOs: `DeviceDto`, `TaskDto`, `CreateTaskDto`, `EventDto`, `HealthDto`, `TelemetryDto`
- `KerenWebSocket` — typed event parsing, no raw JSON to UI
- `ConnectionManager` — CONNECTING/CONNECTED/DISCONNECTED/RECONNECTING/ERROR + exponential backoff reconnect
- `NetworkModule` (Hilt) — OkHttp + Retrofit + Gson

### Still TODO (next checkpoints)

- Repository implementations (`DeviceRepositoryImpl`, `TaskRepositoryImpl`, `EventRepositoryImpl`)
- Dynamic Retrofit base URL from Settings / CoreConfig
- ViewModels for each screen
- Wire screens to StateFlows (remove hardcoded lists)
- Settings screen editable Core URL + Connect button
- Terminal → `POST /tasks` + stdout/stderr from events
- Nervous System: stop infinite demo packets; only animate on real events

### Important note

Until KEREN Core is running and reachable, UI will correctly show **DISCONNECTED / ERROR**.  
That is expected. Do not re-introduce demo data to “look live”.

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

---

## Implementation Priority

### P0
1. ✅ Core API contract defined
2. ✅ REST client + DTOs
3. ✅ WebSocket client + event parser
4. ✅ ConnectionManager (reconnect + backoff)
5. ⬜ Repository implementations
6. ⬜ ViewModels + UI wiring
7. ⬜ Device + Task sync
8. ⬜ Real Terminal task submission

### P1
9. stdout/stderr streaming
10. Live Overview
11. Add Device flow
12. Reconnect → full resync

### P2
13. Event-driven Nervous System (no fake packets)
14. Task trace
15. Auth improvements

---

## Checkpoint history

| ID | Date | Summary |
|----|------|---------|
| CP0 | 2026-08-26 | Full audit. Demo mapped. API contract defined. |
| CP1 | 2026-08-26 | Domain models, repo interfaces, KerenApi, WebSocket, ConnectionManager, NetworkModule |
| CP2 | TBD | Repository impls + ViewModels + Settings connect |
| CP3 | TBD | Terminal real submit + event stream |
| CP4 | TBD | Nervous System real-event only |

---

## Rules (do not violate)

1. No fake CPU/RAM/status/telemetry in production paths.
2. No continuous random packet animation.
3. No hardcoded production Core IPs.
4. UI must show DISCONNECTED when Core is unreachable.
5. After reconnect → resync devices + tasks + resume WS.
6. Single source of truth via repositories → StateFlow → UI.
7. Preserve existing dark theme + navigation + Nervous visual foundation.
