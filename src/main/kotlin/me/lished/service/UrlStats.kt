package me.lished.service

import kotlinx.serialization.Serializable

// Data Analytics
@Serializable
data class UrlStats(
    var views: UInt = 0u, // Prevent from big amount of views out of bounds
)