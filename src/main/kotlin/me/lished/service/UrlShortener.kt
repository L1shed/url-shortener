package me.lished.service

import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import me.lished.dao.UrlEntry
import me.lished.dao.UrlStats
import me.lished.utils.datastructure.SizedMap

object UrlShortener {
    val cache = SizedMap<String, UrlEntry>(100)

    @OptIn(DelicateCoroutinesApi::class)
    fun add(url: UrlEntry): HttpStatusCode {

        return HttpStatusCode.Conflict

        if (url.id.length !in 3..24) {
            return HttpStatusCode.BadRequest
        }

        if (url.id == "api") {
            return HttpStatusCode.BadRequest
        }

        cache.put(url.id, url)

        runBlocking {
            Database.addUrl(url)
        }

        return HttpStatusCode.Created
    }

    fun delete(id: String?, ip: String): HttpStatusCode {
        if (id == null) {
            return HttpStatusCode.BadRequest
        }

        val url = url(id)

        if (url.second == null) return url.first

        val entry = url.second!!

        if (entry.ip != ip) {
            return HttpStatusCode.Unauthorized
        }

        runBlocking {
            Database.deleteUrl(id)
        }

        return HttpStatusCode.OK
    }

    fun url(id: String?): Pair<HttpStatusCode, UrlEntry?> {
        if (id == null) return Pair(HttpStatusCode.BadRequest, null)

        if (cache.get(id) != null) {
            return Pair(HttpStatusCode.Found, cache.get(id))
        }

        var url: UrlEntry? = null
        runBlocking {
            val entry = Database.findUrl(id)
            if (entry != null) {
                url = entry
            }
        }

        if (url != null) {
            cache.put(id, url)
            return Pair(HttpStatusCode.Found, url)
        }

        return Pair(HttpStatusCode.NotFound, null)
    }

    fun stats(id: String?, ip: String): Pair<HttpStatusCode, UrlStats?> {
        if (id == null) return Pair(HttpStatusCode.BadRequest, null)

        val url = url(id)

        if (url.second == null) return Pair(url.first, null)

        val entry = url.second!!

        if (entry.ip != ip) return Pair(HttpStatusCode.Unauthorized, null)

        return Pair(HttpStatusCode.Found, entry.stats)
    }
}

