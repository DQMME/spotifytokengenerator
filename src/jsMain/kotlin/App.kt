import csstype.ClassName
import dataclass.SpotifyScope
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Window
import org.w3c.dom.get
import org.w3c.dom.url.URLSearchParams
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.b
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tr

private val scope = MainScope()

val app = FC<Props> {
    h1 {
        +"About this project"
    }

    p {
        +"This generator can be used to generate Access and Refresh Tokens for the Spotify Web API. You can simply select the scopes you want and click \"Generate\". You will be redirect to the twitch authorize page and after logging in you will see the Tokens in the fields below. You should only use this tokens for testing. For production you should use your own tokens."
        b {
            +"This site is in not affiliated with Spotify. It's an inofficial tool for developers."
        }
    }

    hr {}

    val clientId = window.getParameter("client_id")
    val accessToken = window.getParameter("access_token")
    val refreshToken = window.getParameter("refresh_token")
    val expiresIn = window.getParameter("expires_in")?.toIntOrNull()

    if (accessToken != null
        && refreshToken != null
        && expiresIn != null
        && clientId != null
    ) {
        h1 {
            +"Your Data"
        }

        h2 {
            +"Client Id"
        }

        input {
            value = clientId
            id = "client-id"
        }

        button {
            +"Copy"
            onClick = {
                val clientIdElement = document.getElementById("client-id")!! as HTMLInputElement
                clientIdElement.select()
                document.execCommand("copy")
            }
        }

        br {}

        h2 {
            +"Access Token"
        }

        input {
            value = accessToken
            id = "access-token"
        }

        button {
            +"Copy"
            onClick = {
                val accessTokenElement = document.getElementById("access-token")!! as HTMLInputElement
                accessTokenElement.select()
                document.execCommand("copy")
            }
        }

        br {}

        h2 {
            +"Refresh Token"
        }

        input {
            value = refreshToken
            id = "refresh-token"
        }

        button {
            +"Copy"
            onClick = {
                val refreshTokenElement = document.getElementById("refresh-token")!! as HTMLInputElement
                refreshTokenElement.select()
                document.execCommand("copy")
            }
        }

        br {}

        h2 {
            +"Expires In"
        }

        input {
            value = expiresIn.toString()
            id = "expires-in"
        }

        button {
            +"Copy"
            onClick = {
                val expiresInElement = document.getElementById("expires-in")!! as HTMLInputElement
                expiresInElement.select()
                document.execCommand("copy")
            }
        }

        br {}
        br {}
        hr {}
    }

    h1 {
        +"Scopes"
    }

    table {
        thead {
            tr {
                td {
                    +"Add Scope"
                }

                td {
                    +"Name"
                }

                td {
                    +"Description"
                }
            }
        }

        tbody {
            SpotifyScope.allScopes().forEach { scope ->
                tr {
                    td {
                        input {
                            className = ClassName("scope-selector")
                            type = InputType.checkbox
                            id = scope
                        }
                    }

                    td {
                        +scope
                    }

                    td {
                        +(SpotifyScope.getDescription(scope) ?: "N/A")
                    }
                }
            }
        }
    }

    br {}

    button {
        className = ClassName("generate-button sep plus-icon")

        +"Generate"
        onClick = {
            val scopes = mutableListOf<String>()
            val selectors = document.getElementsByClassName("scope-selector")

            for (i in 0 until selectors.length) {
                val selector = selectors[i] as HTMLInputElement

                if (selector.checked) {
                    scopes.add(selector.id)
                }
            }

            val scopeValue = scopes.joinToString(" ")

            window.location.href = "/generate?scope=$scopeValue"
        }
    }
}

fun Window.getParameter(key: String) = URLSearchParams(window.location.search).get(key)