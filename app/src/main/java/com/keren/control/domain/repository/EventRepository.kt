package com.keren.control.domain.repository

import com.keren.control.domain.model.KerenEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface EventRepository {
    /** Live stream of parsed Core events */
    val events: SharedFlow<KerenEvent>

    /** Recent events for Logs / Overview activity */
    val recentEvents: StateFlow<List<KerenEvent>>
}
