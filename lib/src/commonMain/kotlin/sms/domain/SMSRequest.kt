package sms.domain


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SMSRequest(
    @SerialName("To")
    val to: String,
    @SerialName("From")
    val from: String,
    @SerialName("Body")
    val body: String
)
