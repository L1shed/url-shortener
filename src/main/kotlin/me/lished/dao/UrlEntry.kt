package me.lished.dao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class UrlEntry(
//    @BsonId
    @SerialName("_id")
    val id: String,
    val ip: String,
    val url: String,
    val private: Boolean = false,
    val stats: UrlStats = UrlStats(),
)