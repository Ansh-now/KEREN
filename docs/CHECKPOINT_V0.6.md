# KEREN V0.6 — Checkpoint Log

**Repo:** https://github.com/Ansh-now/KEREN  
**Last updated:** 2026-08-26  
**Principle:** REAL DATA → REAL EVENTS → REAL EXECUTION → REAL VISUALIZATION

---

## CHECKPOINT 3 — Dynamic Core URL + Terminal + Live Overview/Logs (2026-08-26)

### Added / fixed

**Networking**
- `CoreUrlHolder` — runtime HTTP base from Settings
- OkHttp interceptor rewrites host/port/scheme on every REST call
- `ConnectionManager.updateConfig/connect` writes URL into holder

**Terminal**
- `TerminalViewModel` — `POST /tasks` via TaskRepository
- Listens `task.stdout` / `task.stderr` / started / completed / failed
- **No hardcoded demo output**
- Enter / IME Send submits command
- Shows CONNECTED state; blocks submit if not connected

**Overview**
- Real Core connection label
- Device online/offline counts from DeviceRepository
- Executing / queued / completed / failed from TaskRepository
- LIVE ACTIVITY from EventRepository (empty until events)

**Logs**
- Real event stream from EventRepository

**Navigation**
- Bottom bar: Overview · Devices · Terminal · Nervous · **Settings**
- Tasks + Logs remain in NavHost (deep routes)

### How to use (when Core is running)

1. Settings → HTTP URL (`http://<PC-IP>:8080` or emulator `http://10.0.2.2:8080`)
2. CONNECT
3. Terminal → type `python --version` → Send
4. Task created on Core; stdout appears when Core emits events

### Still TODO (CP4+)

- Nervous System: remove `createDemoSnapshot` + infinite packets; event-driven only
- Tasks tab back on bottom bar or link from Overview
- Add Device dialog
- Full auth / pairing
- KEREN Core + Node Agent (separate from this Android repo)

---

## Checkpoint history

| ID | Date | Summary |
|----|------|---------|
| CP0 | 2026-08-26 | Full audit. API contract defined. |
| CP1 | 2026-08-26 | Domain models, API, WebSocket, ConnectionManager |
| CP2 | 2026-08-26 | Repo impls, ViewModels, Settings, no fake Devices/Tasks |
| CP3 | 2026-08-26 | Dynamic URL, Terminal real submit, Overview/Logs live |
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
