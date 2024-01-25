package sms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch
import sms.domain.SmsRepository


class SmsViewModel(
    private val smsRepository: SmsRepository
) : ViewModel() {

    var state by mutableStateOf(SmsState())

    fun sendSms(
        accountSID: String,
        authToken: String,
        to: String,
        from: String,
        body: String
    ) {

        if (to.isBlank()) {
            state = state.copy(error = "Recipient phone number is needed!")
            return
        }

        if (from.isBlank()) {
            state = state.copy(error = "Sender phone number is needed!")
            return
        }

        if (body.isBlank()) {
            state = state.copy(error = "Body can't be blank!")
            return
        }

        viewModelScope.launch {
            smsRepository.sendSMS(
                accountSID = accountSID,
                authToken = authToken,
                to = to,
                from = from,
                body = body
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



}