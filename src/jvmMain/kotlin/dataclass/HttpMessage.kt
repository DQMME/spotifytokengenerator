package dataclass

import kotlinx.serialization.Serializable

@Serializable
data class HttpMessage(val code: Int, val message: String)
