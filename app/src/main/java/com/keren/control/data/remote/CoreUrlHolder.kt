package com.keren.control.data.remote

import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Runtime Core base URL. Settings → updateConfig writes here;
 * OkHttp interceptor rewrites host so Retrofit hits the real Core.
 */
@Singleton
class CoreUrlHolder @Inject constructor() {
    private val base = AtomicReference("http://127.0.0.1:8080/")

    fun setHttpBaseUrl(url: String) {
        if (url.isBlank()) return
        val normalized = url.trim().trimEnd('/') + "/"
        base.set(normalized)
    }

    fun getHttpBaseUrl(): String = base.get()
}
