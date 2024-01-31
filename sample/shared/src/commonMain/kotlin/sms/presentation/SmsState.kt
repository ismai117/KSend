package sms.presentation



data class SmsState(

    val isLoading: Boolean = false,
    val status: Boolean = false,
    val error: String = "",

    val recipient: String = "",
    val recipientError: String = "",

    val body: String = "",
    val bodyError: String = "",

)
