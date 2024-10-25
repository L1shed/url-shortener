package me.lished.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import me.lished.route.apiRoute
import me.lished.route.urlRoute



fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is IllegalArgumentException -> call.respond(HttpStatusCode.BadRequest)
                is NoSuchElementException -> call.respond(HttpStatusCode.NotFound)
                else -> call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            }
        }

        statusFile(HttpStatusCode.NotFound, filePattern = "error#.html")
    }
    routing {

        apiRoute()

        urlRoute()

    }
}
