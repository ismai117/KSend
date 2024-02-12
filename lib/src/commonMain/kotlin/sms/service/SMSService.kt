package sms.service



import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.basicAuth
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.toMap
import kotlinx.serialization.json.Json


class SMSService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }


    suspend fun sendSMS(
        accountSID: String,
        authToken: String,
        from: String,
        recipient: String,
        body: String
    ): SMSResponse {

        val smsRequest = SMSRequest(
            to = recipient,
            from = from,
            body = body
        )

        val httpResponse = client.submitForm {
            url("https://api.twilio.com/2010-04-01/Accounts/${accountSID}/Messages.json")
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
            header(HttpHeaders.AccessControlAllowOrigin, '*')
            basicAuth(
                username = accountSID,
                password = authToken
            )
            setBody(
                FormDataContent(
                    Parameters.build {
                        append("Body", smsRequest.body)
                        append("From", smsRequest.from)
                        append("To", smsRequest.to)
                    }
                )
            )
        }

        return SMSResponse(
            statusCode = httpResponse.status.value,
            body = httpResponse.bodyAsText(),
            headers = httpResponse.headers.toMap()
        )

    }

}