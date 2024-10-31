package me.lished.dao

import io.ktor.server.plugins.origin
import io.ktor.server.request.userAgent
import io.ktor.server.routing.RoutingRequest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import me.lished.utils.IpUtils


@Serializable
data class UrlStats(
    var totalVisits: UInt = 0u,
    val uniqueVisits: MutableMap<String, Visit> = mutableMapOf(),
) {
    fun visit(request: RoutingRequest) {
        val ip = request.origin.remoteHost
        totalVisits++
        if (ip !in uniqueVisits) {
            val userAgent  = request.userAgent().toString()

            uniqueVisits[ip] = Visit(
                ip = ip,
                timestamp = System.currentTimeMillis(),
                userAgent = userAgent,
                country = runBlocking { IpUtils.countryCode(ip) },
            )
            
        } else {
            uniqueVisits[ip] = uniqueVisits[ip]!!.copy(
                timestamp = System.currentTimeMillis()
            )
        }
    }
}

