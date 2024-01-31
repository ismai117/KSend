package email.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EmailRequest(
    @SerialName("personalizations")
    val personalizations: List<Personalization>,
    @SerialName("from")
    val from: Sender,
    @SerialName("subject")
    val subject: String,
    @SerialName("content")
    val content: List<Content>
)

@Serializable
data class Personalization(
    @SerialName("to")
    val to: List<Recipient>
)

@Serializable
data class Recipient(
    @SerialName("email")
    val email: String
)

@Serializable
data class Sender(
    @SerialName("email")
    val email: String
)

@Serializable
data class Content(
    @SerialName("type")
    val type: String,
    @SerialName("value")
    val value: String
)