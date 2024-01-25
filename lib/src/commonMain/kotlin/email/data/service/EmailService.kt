package email.data.service

import io.ktor.client.statement.HttpResponse
import email.domain.model.EmailRequest
import email.domain.model.Recipient

internal interface EmailService {
    suspend fun sendEmail(
        key: String,
        recipients: List<Recipient>,
        sender: String,
        subject: String,
        content: String
    ): HttpResponse
}