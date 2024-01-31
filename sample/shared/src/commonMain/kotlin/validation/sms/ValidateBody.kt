package validation.sms

import validation.ValidationResult

class ValidateBody {
    operator fun invoke(body: String): ValidationResult {
        if (body.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Sms body is required."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}