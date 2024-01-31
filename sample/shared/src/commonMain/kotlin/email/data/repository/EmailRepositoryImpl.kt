package email.data.repository


import email.service.Recipient
import email.service.EmailService



internal class EmailRepositoryImpl {
    suspend fun sendEmail(
        recipients: List<Recipient>,
        subject: String,
        content: String
    ) {

        val emailService = EmailService()

        val response = emailService.sendEmail(
            apiKey = "",
            from = "",
            recipients = recipients,
            subject = subject,
            content = content
        )

        if (response.statusCode == 202) {
            println("Success")
        }

    }
}