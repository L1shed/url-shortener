package me.lished.dao

import kotlinx.serialization.Serializable

@Serializable
data class Visit(
    val ip: String,
    val timestamp: Long = System.currentTimeMillis(),
    val userAgent: String,
    val country: String,
)
