package util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse


fun generateCodeUrl(
    clientId: String = Config.SPOTIFY_CLIENT_ID,
    redirectUri: String = Config.SPOTIFY_REDIRECT_URI,
    scope: MutableList<String>
) = "https://accounts.spotify.com/authorize?" +
        "response_type=code&" +
        "client_id=$clientId&" +
        "scope=${scope.joinToString(" ")}&" +
        "redirect_uri=$redirectUri"

suspend inline fun <reified T> HttpResponse.bodyOrNull() = try {
    body<T>()
} catch (_: Exception) {
    null
}