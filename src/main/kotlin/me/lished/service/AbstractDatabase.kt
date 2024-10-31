package me.lished.service

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase

abstract class AbstractDatabase<T: Any>(getCollection: MongoDatabase.() -> MongoCollection<T>) {
    val client = MongoClient.create(System.getenv("MONGO_CLIENT_URL"))
    val database = client.getDatabase("url_shortener")
    val collection: MongoCollection<T> = database.getCollection()

    open suspend fun add(element: T) {
        collection.insertOne(element)
    }

    open suspend fun find(id: String): T? {
        var entry: T? = null
        collection.find(filter =  eq("_id", id)).limit(1).collect {
            entry = it as T?
        }

        return entry
    }

    open suspend fun delete(id: String) =
        collection.deleteOne(filter = eq("_id", id)).deletedCount == 0L

}