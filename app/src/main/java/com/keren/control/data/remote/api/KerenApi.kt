package com.keren.control.data.remote.api

import com.keren.control.data.remote.dto.CreateTaskDto
import com.keren.control.data.remote.dto.DeviceDto
import com.keren.control.data.remote.dto.HealthDto
import com.keren.control.data.remote.dto.TaskDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * REST contract for KEREN Core v0.6.
 * Core is the authority — this client only requests state and submits intent.
 */
interface KerenApi {

    @GET("v0.6/health")
    suspend fun health(): HealthDto

    @GET("v0.6/devices")
    suspend fun getDevices(): List<DeviceDto>

    @GET("v0.6/devices/{id}")
    suspend fun getDevice(@Path("id") id: String): DeviceDto

    @GET("v0.6/tasks")
    suspend fun getTasks(): List<TaskDto>

    @GET("v0.6/tasks/{id}")
    suspend fun getTask(@Path("id") id: String): TaskDto

    @POST("v0.6/tasks")
    suspend fun createTask(@Body body: CreateTaskDto): TaskDto

    @POST("v0.6/tasks/{id}/cancel")
    suspend fun cancelTask(@Path("id") id: String)
}
