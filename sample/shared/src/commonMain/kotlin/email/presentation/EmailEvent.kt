package email.presentation

import email.service.Recipient


sealed interface EmailEvent {
    class RECIPIENT(val recipient: String) : EmailEvent
    class SUBJECT(val subject: String) : EmailEvent
    class CONTENT(val content: String) : EmailEvent
    data object SUBMIT : EmailEvent
}