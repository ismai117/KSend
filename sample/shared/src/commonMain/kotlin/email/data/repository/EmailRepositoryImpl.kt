package email.data.repository

import ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import email.service.Recipient
import email.domain.repository.EmailRepository
import email.service.EmailService



internal class EmailRepositoryImpl : EmailRepository {
    override suspend fun sendEmail(
        recipients: List<Recipient>,
        subject: String,
        content: String
    ): Flow<ResultState<Unit>> {
        return callbackFlow {

            try {

                trySend(ResultState.Loading())

                val emailService = EmailService()

                val response = emailService.sendEmail(
                    apiKey = "",
                    from = "",
                    recipients = recipients,
                    subject = subject,
                    content = content
                )

                if (response.statusCode == 202) {
                    trySend(ResultState.Success(null))
                }

            } catch (e: Exception) {

                e.printStackTrace()

                trySend(ResultState.Error(e.message))

            }

            awaitClose {
                close()
            }
        }
    }
}