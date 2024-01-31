package validation.email

import validation.ValidationResult

class ValidateSubject {
    operator fun invoke(subject: String): ValidationResult {
        if (subject.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Email Subject is required."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}