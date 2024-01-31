package email.presentation

import KottieAnimation
import ProgressBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import animateKottieCompositionAsState.animateKottieCompositionAsState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import email.data.repository.SENDER_EMAIL_ADDRESS
import email.di.EmailModule
import kottieComposition.KottieCompositionSpec
import kottieComposition.rememberKottieComposition
import moe.tlaster.precompose.navigation.Navigator


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EmailScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator
) {

//    var showAnimation by remember { mutableStateOf(false) }

    val emailViewModel: EmailViewModel = getViewModel(
        key = "email_app_screen",
        factory = viewModelFactory {
            EmailViewModel(emailRepository = EmailModule.emailRepository)
        }
    )

    val emailState = emailViewModel.state

    val composition = rememberKottieComposition(
        spec = KottieCompositionSpec.Url("https://lottie.host/dd09ef53-b150-4c81-a3e1-b5516e940c31/GY604Ofcp4.json")
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        speed = 1f,
        iterations = 1,
        isPlaying = emailState.status
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    emailViewModel.onEvent(EmailEvent.SUBMIT)
                },
                modifier = modifier
                    .padding(bottom = 24.dp)
                    .width(70.dp)
            ) {
                Text(
                    text = "Send"
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Column {

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {

                            Box(
                                modifier = modifier
                                    .weight(0.18f)
//                                    .border(width = 1.dp, color = Color.Blue)
                            ) {
                                Box(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .heightIn(TextFieldDefaults.MinHeight)
                                        .align(Alignment.TopCenter)
                                ) {
                                    Text(
                                        text = "From",
                                        modifier = modifier
                                            .padding(start = 16.dp)
                                            .align(Alignment.CenterStart)
                                    )
                                }
                            }

                            Column(
                                modifier = modifier
                                    .weight(1f)
//                                    .border(width = 1.dp, color = Color.Blue)
                            ) {
                                TextField(
                                    value = "",
                                    onValueChange = {},
                                    modifier = modifier.fillMaxWidth(),
                                    readOnly = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }

                        }

                        Divider(
                            modifier = modifier.fillMaxWidth(),
                            thickness = 3.dp
                        )

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {

                            Box(
                                modifier = modifier
                                    .weight(0.18f)
//                                    .border(width = 1.dp, color = Color.Blue)
                            ) {
                                Box(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .heightIn(TextFieldDefaults.MinHeight)
                                        .align(Alignment.TopCenter)
                                ) {
                                    Text(
                                        text = "To",
                                        modifier = modifier
                                            .padding(start = 16.dp)
                                            .align(Alignment.CenterStart)
                                    )
                                }
                            }

                            Column(
                                modifier = modifier
                                    .weight(1f)
//                                    .border(width = 1.dp, color = Color.Blue)
                            ) {
                                TextField(
                                    value = emailState.recipient,
                                    onValueChange = {
                                        emailViewModel.onEvent(EmailEvent.RECIPIENT(it))
                                    },
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                if (emailState.recipient.isNotBlank()) {
                                                    emailViewModel.addRecipient(emailState.recipient)
                                                }
                                            },
                                            enabled = emailState.recipient.isNotBlank()
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Add Recipient"
                                            )
                                        }
                                    },
                                    isError = emailState.recipientsError.isNotBlank(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )

                                FlowRow(
                                    modifier = modifier
                                        .fillMaxWidth()
                                ) {
                                    emailViewModel.recipients
                                        .forEach {
                                            SuggestionChip(
                                                onClick = {},
                                                label = {
                                                    Text(
                                                        text = it
                                                    )
                                                }
                                            )
                                        }
                                }
                            }

                        }

                    }

                    Divider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 3.dp
                    )

                    TextField(
                        value = emailState.subject,
                        onValueChange = {
                            emailViewModel.onEvent(EmailEvent.SUBJECT(it))
                        },
                        modifier = modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Subject"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        isError = emailState.subjectError.isNotBlank()
                    )

                    Divider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 3.dp
                    )

                    TextField(
                        value = emailState.content,
                        onValueChange = {
                            emailViewModel.onEvent(EmailEvent.CONTENT(it))
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f),
                        placeholder = {
                            Text(
                                text = "Content"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        isError = emailState.recipientsError.isNotBlank()
                    )


                }

                ProgressBar(isLoading = emailState.isLoading)

                if (emailState.status) {
                    KottieAnimation(
                        composition = composition,
                        progress = { animationState.progress },
                        modifier = modifier
                            .fillMaxSize(),
                    )
                }

            }
        }
    )

    LaunchedEffect(
        key1 = animationState.isPlaying
    ) {
        if (animationState.isPlaying) {
            println("Animation Playing")
        }
        if (animationState.isCompleted) {
            println("Animation Completed")
            emailViewModel.clear()
        }
    }

}

