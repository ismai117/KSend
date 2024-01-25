package sms.data.repository

import ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import sms.data.service.SmsService
import sms.domain.SMSRequest
import sms.domain.SmsRepository


internal class SmsRepositoryImpl(
    private val smsService: SmsService
): SmsRepository {
    override suspend fun sendSMS(
        accountSID: String,
        authToken: String,
        to: String,
        from: String,
        body: String
    ): Flow<ResultState<Unit>> {
        return callbackFlow {
            try {
                trySend(ResultState.Loading())
                val response = smsService.sendSMS(accountSID, authToken, to, from, body)
                if (response.status.value == 201) {
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