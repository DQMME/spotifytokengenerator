package dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RedirectUriSession(@SerialName("redirect_uri") val redirectUri: String)