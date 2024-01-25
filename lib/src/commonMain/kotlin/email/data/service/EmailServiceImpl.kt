package email.data.service



import email.domain.model.Content
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import email.domain.model.EmailRequest
import email.domain.model.Personalization
import email.domain.model.Recipient
import email.domain.model.Sender


internal class EmailServiceImpl : EmailService {

    private val client = HttpClient {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
//                    Napier.d(tag = "ktor", message = message)
                }
            }
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }


    override suspend fun sendEmail(
        key: String,
        recipients: List<Recipient>,
        sender: String,
        subject: String,
        content: String
    ): HttpResponse {

        if (key.isBlank()) {
            throw Exception("Send Grid key is needed for this feature")
        }

        val emailRequest = EmailRequest(
            personalizations = listOf(
                Personalization(
                    to = recipients
                )
            ),
            from = Sender(
                email = sender
            ),
            subject = subject,
            content = listOf(
                Content(
                    type = "text/plain",
                    value = content
                )
            ),
        )

        println("service Request. key: $key, email: $emailRequest")

        return client.post {
            url("https://api.sendgrid.com/v3/mail/send")
            header(HttpHeaders.Authorization, "Bearer $key")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            method = HttpMethod.Post
            setBody<EmailRequest>(emailRequest)
        }
    }


}