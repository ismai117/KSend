package email.presentation



data class EmailState(

    val isLoading: Boolean = false,
    val status: Boolean = false,
    val error: String = "",

    val recipient: String = "",
    val recipientsError: String = "",

    val subject: String = "",
    val subjectError: String = "",

    val content: String = "",
    val contentError: String = ""

)
