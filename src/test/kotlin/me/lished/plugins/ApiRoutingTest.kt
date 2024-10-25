package me.lished.plugins

import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.*
import me.lished.module
import me.lished.service.urlStore
import kotlin.test.*

class ApiRoutingTest {

    @Test
    fun manage_urls() = testApplication {
        application {
            module()
        }


        val id = "123"

        // Add URL
        val addResponse = client.post("/api/v1/url/create") {
            setBody(id)
        }
        assertEquals(HttpStatusCode.Created, addResponse.status)
        assertEquals("URL created with ID: $id", addResponse.bodyAsText())

        // Get URL !! todo: in another test
//        val getResponse = client.get("/$id")
//        assertEquals(HttpStatusCode.Found, getResponse.status) // May not be correct
//        assertEquals("https://google.com", getResponse.headers[HttpHeaders.Location])

        // Retry Add URL (should conflict)
        val retryAddResponse = client.post("/api/v1/url/create") {
            setBody(id)
        }
        assertEquals(HttpStatusCode.Conflict, retryAddResponse.status)
        assertEquals("URL with ID: $id already exists", retryAddResponse.bodyAsText())

        // Delete URL
        val deleteResponse = client.delete("/api/v1/url/123/delete")
        assertEquals(HttpStatusCode.OK, deleteResponse.status)
        assertEquals("URL with ID: $id deleted", deleteResponse.bodyAsText())

        val getResponse = client.get("/$id")
        assertEquals(HttpStatusCode.NotFound, getResponse.status)
    }

    @Test
    fun manage_authorizations() = testApplication {
        application {
            module()
        }

        val id = "123"

        // Manually add url with fake ip
        urlStore[id] = UrlEntry(
            id, "not-an-ip", "https://google.com"
        )

        // Try to delete url
        val deleteResponse = client.delete("/api/v1/url/$id/delete")
        assertEquals(HttpStatusCode.Unauthorized, deleteResponse.status)
    }

    @Test
    fun manage_private() {} // todo
}