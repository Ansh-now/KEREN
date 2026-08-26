package com.keren.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.keren.control.domain.model.KerenEvent
import com.keren.control.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    eventRepository: EventRepository
) : ViewModel() {
    val recentEvents: StateFlow<List<KerenEvent>> = eventRepository.recentEvents
}
