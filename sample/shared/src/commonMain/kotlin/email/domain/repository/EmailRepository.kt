package email.domain.repository

import ResultState
import kotlinx.coroutines.flow.Flow
import email.service.Recipient

interface EmailRepository {
    suspend fun sendEmail(
        recipients: List<Recipient>,
        subject: String,
        content: String
    ): Flow<ResultState<Unit>>
}