package com.myapplication

import KSend.sample.wearApp.BuildConfig
import MainView
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.snackbar.SnackbarHost
import com.google.android.horologist.compose.snackbar.SnackbarHostState
import email.service.EmailService
import email.service.Recipient
import kotlinx.coroutines.launch
import sms.service.SMSService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
             App()
        }
    }

    @Composable
    fun App(
        modifier: Modifier = Modifier
    ) {

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

        Scaffold {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                                        content = "Testing from Wear Watch"
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
                                        body = "KSend Sample App - Testing from Wear Watch"
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

}
