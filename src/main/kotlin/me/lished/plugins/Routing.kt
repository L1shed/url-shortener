package me.lished.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.lished.route.apiRoute
import me.lished.route.urlRoute

fun Application.configureRouting() {
    install(StatusPages) {
//        exception<Throwable> { call, cause ->
//            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
//        }

        status(HttpStatusCode.Unauthorized) {
            call.respondText("You are not authorized to access this resource", status = it)
        }

        status(HttpStatusCode.BadRequest) {
            call.respondText("Bad request, please refer to the API documentation", status = it)
        }

        statusFile(HttpStatusCode.NotFound, filePattern = "error#.html")
    }
    routing {

        apiRoute()

        urlRoute()

    }
}
