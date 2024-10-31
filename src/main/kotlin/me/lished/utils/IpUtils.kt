package me.lished.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object IpUtils {

    val client = HttpClient(Java) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun countryCode(ip: String): String {
        @Serializable
        data class Response(val countryCode: String? = null)

        val response: HttpResponse = client.get("https://ipinfo.io/$ip/json")
        if (response.status.value == 200) {
            val countryResponse: Response = response.body()
            return countryResponse.countryCode ?: "Unknown"
        }
        return "Unknown"
    }

}