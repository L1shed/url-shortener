package me.lished.dao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Visit(
//    @SerialName("_id")
    val ip: String,
    val timestamp: Long = System.currentTimeMillis(),
    val userAgent: String,
    val country: String,
)
