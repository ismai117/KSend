package validation


data class ValidationResult(
    val successful: Boolean = false,
    val errorMessage: String = ""
)