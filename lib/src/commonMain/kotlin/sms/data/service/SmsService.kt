package sms.data.service

import io.ktor.client.statement.HttpResponse
import sms.domain.SMSRequest


internal interface SmsService {
    suspend fun sendSMS(
        accountSID: String,
        authToken: String,
        to: String,
        from: String,
        body: String
    ): HttpResponse
}