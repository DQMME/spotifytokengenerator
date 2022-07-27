package dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateUrlRequest(@SerialName("redirect_uri") val redirectUri: String, val scope: String)

@Serializable
data class GenerateUrlResponse(val url: String)