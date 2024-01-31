package sms.service

data class SMSResponse(
    val statusCode: Int,
    val body: String,
    val headers: Map<String, List<String>>
)
