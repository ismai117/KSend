package validation.sms

import validation.ValidationResult
class ValidateRecipientPhoneNumber {
    operator fun invoke(phoneNumber: String): ValidationResult {
        if (phoneNumber.isEmpty()){
            return ValidationResult(
                successful = false,
                errorMessage = "Recipient phone number is required. Please provide a valid phone number."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}