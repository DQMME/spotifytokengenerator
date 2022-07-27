package api

import dataclass.SpotifyAccessTokenResponse
import dataclass.SpotifyTokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import util.Config
import util.bodyOrNull

object SpotifyAPI {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    suspend fun requestAccessToken(
        code: String,
        clientId: String = Config.SPOTIFY_CLIENT_ID,
        clientSecret: String = Config.SPOTIFY_CLIENT_SECRET,
        redirectUri: String = Config.SPOTIFY_REDIRECT_URI
    ) =
        httpClient.submitForm("https://accounts.spotify.com/api/token", formParameters = Parameters.build {
            append("code", code)
            append("grant_type", "authorization_code")
            append("client_id", clientId)
            append("redirect_uri", redirectUri)
            append("client_secret", clientSecret)
        }) {}.bodyOrNull<SpotifyTokenResponse>()

    suspend fun refreshAccessToken(
        refreshToken: String,
        clientId: String = Config.SPOTIFY_CLIENT_ID,
        clientSecret: String = Config.SPOTIFY_CLIENT_SECRET,
        redirectUri: String = Config.SPOTIFY_REDIRECT_URI
    ) =
        httpClient.submitForm("https://accounts.spotify.com/api/token", formParameters = Parameters.build {
            append("refresh_token", refreshToken)
            append("grant_type", "refresh_token")
            append("client_id", clientId)
            append("redirect_uri", redirectUri)
            append("client_secret", clientSecret)
        }) {}.bodyOrNull<SpotifyAccessTokenResponse>()
}