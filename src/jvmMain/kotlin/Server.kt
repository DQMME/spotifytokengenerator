import api.SpotifyAPI
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import util.Config
import util.generateCodeUrl

fun main() {
    embeddedServer(Netty, 9090) {
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

            static("/static") {
                resources("")
            }
        }
    }.start(wait = true)
}

suspend fun ApplicationCall.respondResourceHtml(name: String) = respondText(
    this::class.java.classLoader.getResource(name)!!.readText(),
    ContentType.Text.Html
)