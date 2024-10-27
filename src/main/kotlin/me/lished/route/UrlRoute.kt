package me.lished.route

import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import me.lished.service.UrlShortener

fun Routing.urlRoute() {
    get("/{id}") {
        val id = call.parameters["id"]
        val status = UrlShortener.url(id)

        if (status.second == null) {
            call.respond(status.first)
        } else {
            call.respondRedirect(status.second!!.url)
            status.second!!.stats.visit(call.request)
        }
    }
}