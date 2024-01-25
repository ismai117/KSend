package sms.domain

import ResultState
import kotlinx.coroutines.flow.Flow


interface SmsRepository {
    suspend fun sendSMS(
         accountSID: String,
         authToken: String,
         to: String,
         from: String,
         body: String
    ): Flow<ResultState<Unit>>
}