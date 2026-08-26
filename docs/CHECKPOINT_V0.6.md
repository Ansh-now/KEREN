# KEREN V0.6 — Checkpoint Log

**Repo:** https://github.com/Ansh-now/KEREN  
**Last updated:** 2026-08-26  
**Principle:** REAL DATA → REAL EVENTS → REAL EXECUTION → REAL VISUALIZATION

---

## CHECKPOINT 2 — Repositories + ViewModels + Settings Connect (2026-08-26)

### Added

**Implementations**
- `DeviceRepositoryImpl` — REST refresh + WS device events
- `TaskRepositoryImpl` — REST + createTask + queued/executing/history StateFlows
- `EventRepositoryImpl` — recent event buffer from WebSocket
- DTO → Domain mappers
- Hilt `RepositoryModule`

**ViewModels**
- `ConnectionViewModel` — config, connect, disconnect, resync
- `DevicesViewModel`
- `TasksViewModel` — includes `submitCommand`

**UI wiring**
- `SettingsScreen` — HTTP URL, WS URL, token, CONNECT / DISCONNECT / RESYNC, live connection state
- `DevicesScreen` — **hardcoded devices removed**; shows empty + "Not connected" until Core responds
- `TasksScreen` — **hardcoded tasks removed**; shows empty queues until Core data arrives

### Behavior now

1. Open Settings
2. Set Core HTTP URL (phone: PC LAN IP; emulator: `http://10.0.2.2:8080`)
3. CONNECT
4. Connection state updates (CONNECTING → CONNECTED / ERROR)
5. Devices / Tasks stay empty until Core returns real data — **no fake rows**

### Still TODO

- Dynamic Retrofit base URL when config changes (currently placeholder base in NetworkModule)
- Terminal real `POST /tasks` + stdout/stderr stream
- Overview + Logs from EventRepository
- Nervous System: remove infinite demo packets
- Add Device dialog (P1)

### Note on Retrofit base URL

`NetworkModule` still uses placeholder `http://127.0.0.1:8080/`.  
Next small fix: rebuild Retrofit from `CoreConfig.httpBaseUrl` or use an interceptor so REST calls hit the URL entered in Settings.

---

## Checkpoint history

| ID | Date | Summary |
|----|------|---------|
| CP0 | 2026-08-26 | Full audit. Demo mapped. API contract defined. |
| CP1 | 2026-08-26 | Domain models, interfaces, KerenApi, WebSocket, ConnectionManager |
| CP2 | 2026-08-26 | Repo impls, ViewModels, Settings connect, Devices/Tasks no fake data |
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
