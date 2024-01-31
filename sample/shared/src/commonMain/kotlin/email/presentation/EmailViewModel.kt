package email.presentation

import ResultState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch
import email.service.Recipient
import email.domain.repository.EmailRepository
import kotlinx.coroutines.Dispatchers
import validation.email.ValidateContent
import validation.email.ValidateRecipientEmail
import validation.email.ValidateSubject


class EmailViewModel(
    private val emailRepository: EmailRepository
): ViewModel() {

    private val validateRecipientEmail = ValidateRecipientEmail()
    private val validateSubject = ValidateSubject()
    private val validateContent = ValidateContent()

    var state by mutableStateOf(EmailState())

    val recipients = mutableStateListOf<String>()

    fun onEvent(event: EmailEvent) {
        when (event) {
            is EmailEvent.RECIPIENT -> {
                state = state.copy(recipient = event.recipient)
            }

            is EmailEvent.SUBJECT -> {
                state = state.copy(subject = event.subject)
            }

            is EmailEvent.CONTENT -> {
                state = state.copy(content = event.content)
            }

            EmailEvent.SUBMIT -> {
                sendEmail()
            }
        }
    }

    private fun sendEmail() {

        val recipientsResult = validateRecipientEmail(email = recipients.toSet().toList())
        val subjectResult = validateSubject(subject = state.subject)
        val contentResult = validateContent(content = state.content)

        val hasError = listOf(
            recipientsResult,
            subjectResult,
            contentResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                recipientsError = recipientsResult.errorMessage,
                subjectError = subjectResult.errorMessage,
                contentError = contentResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            emailRepository.sendEmail(
                recipients = recipients.map { Recipient(email = it) },
                subject = state.subject,
                content = state.content
            ).collect { result ->
                state = state.copy(isLoading = false, status = false, error = "")
                when (result) {
                    is ResultState.Loading -> {
                        state = state.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        state = state.copy(
                            isLoading = false,
                            status = true
                        )
                    }

                    is ResultState.Error -> {
                        result.error?.let { error ->
                            state = state.copy(
                                isLoading = false,
                                status = false,
                                error = error
                            )
                        }
                    }
                }
            }
        }
    }

    fun addRecipient(recipient: String) {
        if (!recipients.contains(recipient)) {
            recipients.add(recipient)
        }
    }

    fun clear() {
        state = state.copy(
            status = false,
            recipient = "",
            subject = "",
            content = ""
        )
        recipients.clear()
    }

}
