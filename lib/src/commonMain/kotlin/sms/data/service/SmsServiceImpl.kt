package sms.data.service


import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.basicAuth
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import sms.domain.SMSRequest


internal class SmsServiceImpl : SmsService {


    private val client = HttpClient {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
//                    Napier.d(tag = "ktor", message = message)
                }
            }
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }


    override suspend fun sendSMS(
        accountSID: String,
        authToken: String,
        to: String,
        from: String,
        body: String
    ): HttpResponse {

        val smsRequest = SMSRequest(
            to = to,
            from = from,
            body = body
        )

        return client.post {
            url("https://api.twilio.com/2010-04-01/Accounts/$accountSID/Messages.json")
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
            basicAuth(
                username = accountSID,
                password = authToken
            )
            setBody(
                FormDataContent(
                    Parameters.build {
                        append("To", smsRequest.to)
                        append("From", smsRequest.from)
                        append("Body", smsRequest.body)
                    }
                )
            )
        }
    }

}