package email.service


data class EmailResponse(
    val statusCode: Int,
    val body: String,
    val headers: Map<String, List<String>>
)
