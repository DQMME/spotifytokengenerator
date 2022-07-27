package util

import dataclass.HttpMessage
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.server.response.respondText

fun generateCodeUrl(
    clientId: String = Config.SPOTIFY_CLIENT_ID,
    redirectUri: String = Config.SPOTIFY_REDIRECT_URI,
    scope: MutableList<String>
) = "https://accounts.spotify.com/authorize?" +
        "response_type=code&" +
        "client_id=$clientId&" +
        "scope=${scope.joinToString(" ")}&" +
        "redirect_uri=$redirectUri"
fun generateCallbackUrl(
    redirectUri: String,
    scope: MutableList<String>
) = "${Config.ENDPOINT}/api/call" +
        "?scope=${scope.joinToString(" ")}&" +
        "redirect_uri=$redirectUri"

suspend inline fun <reified T> HttpResponse.bodyOrNull() = try {
    body<T>()
} catch (_: Exception) {
    null
}

suspend fun ApplicationCall.respondMessage(message: String, statusCode: HttpStatusCode) =
    respond(HttpMessage(statusCode.value, message))

suspend fun ApplicationCall.respondResourceHtml(name: String) = respondText(
    this::class.java.classLoader.getResource(name)!!.readText(),
    ContentType.Text.Html
)