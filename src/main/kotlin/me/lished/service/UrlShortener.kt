package me.lished.service

import io.ktor.http.HttpStatusCode

object UrlShortener {
    val cache = mutableMapOf<String, UrlEntry>()

    fun add(url: UrlEntry): HttpStatusCode {
        // Check if url with same id already exists
        if (url.id in cache) {
            return HttpStatusCode.Conflict
        }

        // Check if id length is between 3 and 24
        if (url.id.length !in 3..24) {
            return HttpStatusCode.BadRequest
        }

        if (url.id == "api") {
            return HttpStatusCode.BadRequest
        }

        cache[url.id] = url
        return HttpStatusCode.Created
    }

    fun delete(id: String?, ip: String): HttpStatusCode {
        if (id == null) {
            return HttpStatusCode.BadRequest
        }

        // Check if exists
        val url = cache[id] ?: return HttpStatusCode.NotFound

        // Check if ip matches
        if (url.ip != ip) {
            return HttpStatusCode.Unauthorized
        }

        cache.remove(id)
        return HttpStatusCode.OK
    }

    fun url(id: String?): Pair<HttpStatusCode, UrlEntry?> {
        if (id == null) return Pair(HttpStatusCode.BadRequest, null)
        val url = cache[id] ?: return Pair(HttpStatusCode.NotFound, null)
        url.stats.views++
        return Pair(HttpStatusCode.Found, url)
    }

    fun stats(id: String?, ip: String): Pair<HttpStatusCode, UrlStats?> {
        if (id == null) return Pair(HttpStatusCode.BadRequest, null)
        val url = cache[id] ?: return Pair(HttpStatusCode.NotFound, null)
        if (url.ip != ip) return Pair(HttpStatusCode.Unauthorized, null)
        return Pair(HttpStatusCode.Found, url.stats)
    }
}

