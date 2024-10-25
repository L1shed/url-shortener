package me.lished.service

import kotlinx.serialization.Serializable

@Serializable
data class UrlEntry(
    val id: String,
    val ip: String,
    val url: String,
    val private: Boolean = false,
    val stats: UrlStats = UrlStats(),
)