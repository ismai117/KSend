package email

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch
import email.domain.model.Recipient
import email.domain.repository.EmailRepository
import validation.ValidateRecipientEmail
import validation.ValidateSenderEmail


class EmailViewModel(
    private val emailRepository: EmailRepository
): ViewModel() {

    private val validateRecipientEmail = ValidateRecipientEmail()
    private val validateSenderEmail = ValidateSenderEmail()

    var state by mutableStateOf(EmailState())

    fun sendEmail(
        key: String,
        recipients: List<Recipient>,
        sender: String,
        subject: String,
        content: String
    ){

        val recipientsResult = validateRecipientEmail(email = recipients.map { it.email } )
        val senderResult = validateSenderEmail(email = sender)

        val hasError = listOf(
            recipientsResult,
            senderResult
        ).any { !it.successful }

        if (hasError){
            state = state.copy(
                senderError = senderResult.errorMessage,
                recipientsError = recipientsResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            emailRepository.sendEmail(
                key = key,
                recipients = recipients,
                sender = sender,
                subject = subject,
                content = content
            ).collect { result ->
                state = state.copy(isLoading = false, status = false, error = "")
                when(result){
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

    fun clearErrorMessage(){
        state = state.copy(
            senderError = "",
            recipientsError = ""
        )
    }

}