package sms.presentation


sealed class SmsEvent {
    class RECIPIENT(val recipient: String) : SmsEvent()
    class BODY(val body: String) : SmsEvent()
   object SUBMIT : SmsEvent()
}