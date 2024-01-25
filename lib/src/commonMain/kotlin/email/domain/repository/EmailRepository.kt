package email.domain.repository

import ResultState
import kotlinx.coroutines.flow.Flow
import email.domain.model.Recipient

interface EmailRepository {
    suspend fun sendEmail(
        key: String,
        recipients: List<Recipient>,
        sender: String,
        subject: String,
        content: String
    ): Flow<ResultState<Unit>>
}