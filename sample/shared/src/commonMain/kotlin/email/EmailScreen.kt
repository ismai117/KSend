package email

import KSend.sample.shared.BuildConfig
import ProgressBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import email.domain.model.Recipient
import email.factory.EmailRepositoryFactory
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator


@Composable
fun EmailScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val emailViewModel: EmailViewModel = getViewModel(
        key = "email_app_screen",
        factory = viewModelFactory {
            EmailViewModel(emailRepository = EmailRepositoryFactory())
        }
    )

    val emailState = emailViewModel.state

    var recipient by remember { mutableStateOf("") }
    val recipients = remember { mutableStateListOf<String>() }

    var subject by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(
        key1 = emailState.recipientsError,
        key2 = emailState.senderError
    ) {
        if (emailState.recipientsError.isNotBlank()) {
            snackbarHostState.showSnackbar(emailState.recipientsError)
            emailViewModel.clearErrorMessage()
        }
        if (emailState.senderError.isNotBlank()) {
            snackbarHostState.showSnackbar(emailState.senderError)
            emailViewModel.clearErrorMessage()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) { data ->
                Box(
                    modifier = modifier.fillMaxSize()
                ) {
                    Snackbar(
                        modifier = modifier
                            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                            .align(Alignment.TopCenter),
                        action = {}
                    ) {
                        Text(
                            text = data.visuals.message
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            Column(
                modifier = modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Column {
                    TextField(
                        value = recipient,
                        onValueChange = {
                            recipient = it
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (recipient.isNotBlank()){
                                        recipients.add(recipient)
                                    }
                                },
                                enabled = recipient.isNotBlank()
                            ){
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Recipient"
                                )
                            }
                        },
                        label = {
                            Text(
                                text = "Recipient"
                            )
                        }
                    )
                    Column {
                        recipients.toSet().toList()
                            .forEach {
                                Text(
                                    text = it
                                )
                            }
                    }
                }

                TextField(
                    value = subject,
                    onValueChange = {
                        subject = it
                    },
                    label = {
                        Text(
                            text = "Subject"
                        )
                    }
                )

                TextField(
                    value = content,
                    onValueChange = {
                        content = it
                    },
                    label = {
                        Text(
                            text = "Content"
                        )
                    }
                )

                Button(
                    onClick = {
                        emailViewModel.sendEmail(
                            key = BuildConfig.API_KEY,
                            recipients = recipients.map {
                                Recipient(
                                    email = it
                                )
                            },
                            sender = BuildConfig.SENDER_EMAIL_ADDRESS,
                            subject = subject,
                            content = content
                        )
                    }
                ) {
                    Text(
                        text = "Send Email"
                    )
                }


            }

            ProgressBar(isLoading = emailState.isLoading)

        }

    }


}

