package validation


private val emailAddressRegex = Regex(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

class ValidateSenderEmail {
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Email address is required. Please provide a valid email."
            )
        }
        if (!email.matches(emailAddressRegex)){
            return ValidationResult(
                successful = false,
                errorMessage = "Invalid email. Please enter a valid email address."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}