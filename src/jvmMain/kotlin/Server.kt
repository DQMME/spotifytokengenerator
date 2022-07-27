import api.SpotifyAPI
import dataclass.GenerateUrlRequest
import dataclass.GenerateUrlResponse
import dataclass.RedirectUriSession
import dataclass.RefreshTokenRequest
import dataclass.RequestTokenRequest
import dataclass.RequestTokenResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.receiveOrNull
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.server.util.url
import kotlinx.serialization.json.Json
import util.Config
import util.generateCallbackUrl
import util.generateCodeUrl
import util.respondMessage
import util.respondResourceHtml

fun main() {
    embeddedServer(Netty, 9090) {
        install(Sessions) {
            cookie<RedirectUriSession>("auth")
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }

        install(CORS) {
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            anyHost()
        }

        routing {
            get("/") {
                call.respondResourceHtml("index.html")
            }

            get("/callback") {
                val code = call.parameters["code"]

                if (code == null) {
                    call.respondRedirect("/")
                    return@get
                }

                val token = SpotifyAPI.requestAccessToken(code)

                if (token == null) {
                    call.respondRedirect("/")
                    return@get
                }

                call.respondRedirect("/?client_id=${Config.SPOTIFY_CLIENT_ID}&access_token=${token.accessToken}&refresh_token=${token.refreshToken}&expires_in=${token.expiresIn}")
            }

            get("/generate") {
                val givenScope = call.parameters["scope"]?.split(" ")?.toMutableList()

                if (givenScope == null) {
                    call.respondRedirect("/")
                    return@get
                }

                call.respondRedirect(generateCodeUrl(scope = givenScope))
            }

            route("/api") {
                get("/call") {
                    val redirectUri = call.parameters["redirect_uri"]
                    val providedScope = call.parameters["scope"]?.split(" ")?.toMutableList()

                    if (redirectUri == null || providedScope == null) {
                        call.respondMessage("No redirect uri or scope provided", HttpStatusCode.BadRequest)
                        return@get
                    }

                    call.sessions.clear<RedirectUriSession>()
                    call.sessions.set(RedirectUriSession(redirectUri))
                    call.respondRedirect(
                        generateCodeUrl(
                            scope = providedScope,
                            redirectUri = redirectUri
                        )
                    )
                }

                get("/spotifycallback") {
                    val session = call.sessions.get<RedirectUriSession>()

                    if (session == null) {
                        call.respondMessage("You don't have a redirect uri cookie set.", HttpStatusCode.BadRequest)
                        return@get
                    }

                    val code = call.parameters["code"]

                    if (code == null) {
                        call.respondMessage("The url doesn't contain a code.", HttpStatusCode.BadRequest)
                        return@get
                    }

                    call.respondRedirect(session.redirectUri + "?code=$code")
                }

                post("/generate-url") {
                    val request = call.receiveOrNull<GenerateUrlRequest>()

                    if (request == null) {
                        call.respondMessage("No scope provided", HttpStatusCode.BadRequest)
                        return@post
                    }

                    call.respond(
                        GenerateUrlResponse(
                            generateCallbackUrl(
                                request.redirectUri,
                                request.scope.split(" ").toMutableList()
                            )
                        )
                    )
                }

                post("/request-token") {
                    val request = call.receiveOrNull<RequestTokenRequest>()

                    if (request == null) {
                        call.respondMessage("No code provided", HttpStatusCode.BadRequest)
                        return@post
                    }

                    val token = SpotifyAPI.requestAccessToken(
                        code = request.code,
                        redirectUri = Config.API_SPOTIFY_REDIRECT_URI
                    )

                    if (token == null) {
                        call.respondMessage(
                            "An error occurred while generating the token",
                            HttpStatusCode.InternalServerError
                        )
                        return@post
                    }

                    call.respond(
                        RequestTokenResponse(
                            Config.SPOTIFY_CLIENT_ID,
                            token.accessToken,
                            token.refreshToken,
                            token.expiresIn
                        )
                    )
                }

                post("/refresh-token") {
                    val request = call.receiveOrNull<RefreshTokenRequest>()

                    if (request == null) {
                        call.respondMessage("No refresh token provided", HttpStatusCode.BadRequest)
                        return@post
                    }

                    val token = SpotifyAPI.refreshAccessToken(
                        refreshToken = request.refreshToken,
                        redirectUri = Config.API_SPOTIFY_REDIRECT_URI
                    )

                    if (token == null) {
                        call.respondMessage(
                            "An error occurred while refreshing the token",
                            HttpStatusCode.InternalServerError
                        )
                        return@post
                    }

                    call.respond(
                        RequestTokenResponse(
                            Config.SPOTIFY_CLIENT_ID,
                            token.accessToken,
                            request.refreshToken,
                            token.expiresIn
                        )
                    )
                }
            }

            static("/static") {
                resources("")
            }
        }
    }.start(wait = true)
}