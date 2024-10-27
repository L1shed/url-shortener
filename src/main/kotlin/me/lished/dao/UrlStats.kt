package me.lished.dao

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.origin
import io.ktor.server.request.userAgent
import io.ktor.server.routing.RoutingRequest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val client = HttpClient(Java) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

@Serializable
data class UrlStats(
    var totalVisits: UInt = 0u,
    val uniqueVisits: MutableMap<String, Visit> = mutableMapOf(),
) {
    fun visit(request: RoutingRequest) {
        val ip = request.origin.remoteHost
        ULong
        totalVisits++
        if (ip !in uniqueVisits) {
            val userAgent  = request.userAgent().toString()

            uniqueVisits[ip] = Visit(
                ip = ip,
                timestamp = System.currentTimeMillis(),
                userAgent = userAgent,
                country = getCountryCode(ip),
            )
            
        } else {
            uniqueVisits[ip] = uniqueVisits[ip]!!.copy(
                timestamp = System.currentTimeMillis()
            )
        }
    }
}

fun getCountryCode(ip: String): String {
    return "Unknown"

    @Serializable
    data class Response(val countryCode: String? = null)

    runBlocking {
        val response: HttpResponse = client.get("https://ipinfo.io/$ip/json")
        if (response.status.value == 200) {
            val countryResponse: Response = response.body()
            return@runBlocking countryResponse.countryCode ?: "Unknown"
        }
    }
    return "Unknown"
}