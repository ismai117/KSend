package email



data class EmailState(

    val isLoading: Boolean = false,
    val status: Boolean = false,
    val error: String = "",

    val senderError: String = "",
    val recipientsError: String = ""

)
