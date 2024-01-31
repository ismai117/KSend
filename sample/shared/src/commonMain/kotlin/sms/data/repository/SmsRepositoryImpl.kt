package sms.data.repository



import sms.service.SMSService


internal class SmsRepositoryImpl {
    suspend fun sendSMS(
        recipient: String,
        body: String
    ) {

        val smsService = SMSService()

        val response = smsService.sendSMS(
            accountSID =  "",
            authToken = "",
            from = "",
            recipient = recipient,
            body = body
        )

        if (response.statusCode == 201) {
            println("Success")
        }

    }
}