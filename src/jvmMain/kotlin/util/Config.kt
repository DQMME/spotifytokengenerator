package util

import dev.schlaubi.envconf.environment

object Config {
    val SPOTIFY_CLIENT_ID by environment
    val SPOTIFY_CLIENT_SECRET by environment
    val SPOTIFY_REDIRECT_URI by environment
    val API_SPOTIFY_REDIRECT_URI by environment
    val ENDPOINT by environment
}