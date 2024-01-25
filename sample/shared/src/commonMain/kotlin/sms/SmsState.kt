package sms



data class SmsState(
    val isLoading: Boolean = false,
    val status: Boolean = false,
    val error: String = "",
)
