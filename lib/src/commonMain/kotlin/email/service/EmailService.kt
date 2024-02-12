package email.service


import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.statement.bodyAsText
import io.ktor.util.toMap


class EmailService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }


    suspend fun sendEmail(
        apiKey: String,
        from: String,
        recipients: List<Recipient>,
        subject: String,
        content: String
    ): EmailResponse {

        if (apiKey.isBlank()) {
            throw Exception("Send Grid key is needed for this feature")
        }

        val emailRequest = EmailRequest(
            personalizations = listOf(
                Personalization(
                    to = recipients
                )
            ),
            from = Sender(
                email = from
            ),
            subject = subject,
            content = listOf(
                Content(
                    type = "text/plain",
                    value = content
                )
            ),
        )

        val httpResponse = client.post {
            url("https://api.sendgrid.com/v3/mail/send")
            header(HttpHeaders.Authorization, "Bearer $apiKey")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.AccessControlAllowOrigin, '*')
            method = HttpMethod.Post

            setBody<EmailRequest>(emailRequest)
        }

        return EmailResponse(
            statusCode = httpResponse.status.value,
            body = httpResponse.bodyAsText(),
            headers = httpResponse.headers.toMap()
        )

    }


}