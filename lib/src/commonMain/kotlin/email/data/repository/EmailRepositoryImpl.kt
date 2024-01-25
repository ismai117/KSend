package email.data.repository

import ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import email.data.service.EmailService
import email.domain.model.Content
import email.domain.model.EmailRequest
import email.domain.model.Personalization
import email.domain.model.Recipient
import email.domain.model.Sender
import email.domain.repository.EmailRepository


internal class EmailRepositoryImpl(
    private val emailService: EmailService
) : EmailRepository {
    override suspend fun sendEmail(key: String,
       recipients: List<Recipient>,
       sender: String,
       subject: String,
       content: String
    ): Flow<ResultState<Unit>> {
        return callbackFlow {
            try {
                trySend(ResultState.Loading())
                val response = emailService.sendEmail(key, recipients, sender, subject, content)
                if (response.status.value == 202){
                    trySend(ResultState.Success(null))
                }
            }catch (e: Exception){
                e.printStackTrace()
                trySend(ResultState.Error(e.message))
            }
            awaitClose{
                close()
            }
        }
    }
}