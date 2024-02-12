import KSend.sample.shared.BuildConfig
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import email.service.EmailService
import email.service.Recipient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sms.service.SMSService


@Composable
internal fun App(
    modifier: Modifier = Modifier
) = MaterialTheme {

    val emailService = EmailService()
    val smsService = SMSService()

    val scope = rememberCoroutineScope()
    val hostState = remember { SnackbarHostState() }

    var isLoading by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf(false) }

    LaunchedEffect(status) {
        if (status) {
            hostState.showSnackbar("Successful")
            status = false
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = hostState) { data ->
                Snackbar {
                    Text(
                        text = data.visuals.message
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            Column {
                Button(
                    onClick = {
                        isLoading = true
                        scope.launch {
                            runCatching {
                                emailService.sendEmail(
                                    apiKey = BuildConfig.API_KEY,
                                    from = BuildConfig.SENDER_EMAIL_ADDRESS,
                                    recipients = listOf(
                                        Recipient(email = "ishyy17@gmail.com")
                                    ),
                                    subject = "KSend Sample App",
                                    content = "Testing"
                                )
                            }.onSuccess { result ->
                                if (result.statusCode == 202) {
                                    isLoading = false
                                    status = true
                                }
                            }.onFailure {
                                it.printStackTrace()
                                isLoading = false
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Email"
                    )
                }

                Button(
                    onClick = {
                        isLoading = true
                        scope.launch {
                            runCatching {
                                smsService.sendSMS(
                                    accountSID = BuildConfig.ACCOUNTSID,
                                    authToken = BuildConfig.AUTHTOKEN,
                                    from = BuildConfig.SENDER_PHONE_NUMBER,
                                    recipient = "+447599946274",
                                    body = "KSend Sample App - Testing"
                                )
                            }.onSuccess { result ->
                                if (result.statusCode == 201) {
                                    isLoading = false
                                    status = true
                                }
                            }.onFailure {
                                it.printStackTrace()
                                isLoading = false
                            }
                        }
                    }
                ) {
                    Text(
                        text = "SMS"
                    )
                }
            }

            if (isLoading) {
                CircularProgressIndicator()
            }

        }
    }

}

expect fun getPlatformName(): String
