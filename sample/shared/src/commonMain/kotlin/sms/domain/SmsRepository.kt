package sms.domain

import ResultState
import kotlinx.coroutines.flow.Flow


interface SmsRepository {
    suspend fun sendSMS(
         recipient: String,
         body: String
    ): Flow<ResultState<Unit>>
}