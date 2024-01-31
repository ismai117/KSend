package sms.data.repository


import ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import sms.domain.SmsRepository
import sms.service.SMSService


internal class SmsRepositoryImpl : SmsRepository {
    override suspend fun sendSMS(
        recipient: String,
        body: String
    ): Flow<ResultState<Unit>> {
        return callbackFlow {
            try {

                trySend(ResultState.Loading())

                val smsService = SMSService()

                val response = smsService.sendSMS(
                    accountSID =  "",
                    authToken = "",
                    from = "",
                    recipient = recipient,
                    body = body
                )

                if (response.statusCode == 201) {
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