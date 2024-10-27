package me.lished.route

import io.ktor.server.plugins.origin
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import me.lished.dao.UrlEntry
import me.lished.service.UrlShortener

fun Routing.apiRoute() {
    get("/api/v1/url/{id}/stats") {
        val id = call.parameters["id"]
        val ip = call.request.origin.remoteHost
        val stats = UrlShortener.stats(id, ip)

        if (stats.second != null) {
            call.respond(stats.second!!)
        } else {
            call.respond(stats.first)
        }
    }

    post("/api/v1/url/create") {
        val id = call.receive<String>()
        val ip = call.request.origin.remoteHost

        val status = UrlShortener.add(
            UrlEntry(
                id,
                ip,
                "https://google.com",
            )
        )

        call.respond(status)
    }

    delete("/api/v1/url/{id}/delete") {
        val id = call.parameters["id"]
        val ip = call.request.origin.remoteHost

        val status = UrlShortener.delete(id, ip)

        call.respond(status)
    }
}

