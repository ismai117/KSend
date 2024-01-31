package validation.email

import validation.ValidationResult


private val emailAddressRegex = Regex(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

class ValidateRecipientEmail {
    operator fun invoke(email: List<String>): ValidationResult {
        if (email.isEmpty()){
            return ValidationResult(
                successful = false,
                errorMessage = "Recipient email address is required. Please provide a valid email."
            )
        }
        if (email.any { !it.matches(emailAddressRegex) }){
            return ValidationResult(
                successful = false,
                errorMessage = "One or more email addresses are invalid. Please check and correct the email addresses."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}