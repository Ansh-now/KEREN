package com.keren.control.di

import com.keren.control.data.connection.ConnectionManager
import com.keren.control.data.repository.DeviceRepositoryImpl
import com.keren.control.data.repository.EventRepositoryImpl
import com.keren.control.data.repository.TaskRepositoryImpl
import com.keren.control.domain.repository.ConnectionRepository
import com.keren.control.domain.repository.DeviceRepository
import com.keren.control.domain.repository.EventRepository
import com.keren.control.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindConnectionRepository(impl: ConnectionManager): ConnectionRepository

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(impl: DeviceRepositoryImpl): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository
}
