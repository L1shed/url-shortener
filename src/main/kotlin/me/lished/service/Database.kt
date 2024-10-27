package me.lished.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.filter
import me.lished.dao.UrlEntry

object Database {
    val client = MongoClient.create("mongodb+srv://styzox0:jpp3ZMy5PXpZPhqu@cluster0.uxx0lsh.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
    val database = client.getDatabase("url_shortener")
    val collection = database.getCollection<UrlEntry>("urls")

    suspend fun addUrl(url: UrlEntry) {
        collection.insertOne(url)
    }

    suspend fun findUrl(id: String): UrlEntry? {
        var entry: UrlEntry? = null
        collection.find<UrlEntry>(eq("_id", id)).limit(1).collect {
            entry = it
        }

        return entry
    }

    suspend fun deleteUrl(id: String) {
        val res =collection.deleteOne(eq("_id", id))
        if (res.deletedCount == 0L) {
            throw Exception("No document found")
        }
    }
}