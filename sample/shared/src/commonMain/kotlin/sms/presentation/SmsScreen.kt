package sms.presentation


import KottieAnimation
import ProgressBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.sp
import animateKottieCompositionAsState.animateKottieCompositionAsState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kottieComposition.KottieCompositionSpec
import kottieComposition.rememberKottieComposition
import moe.tlaster.precompose.navigation.Navigator
import sms.di.SmsModule


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmsScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator
) {

    val smsViewModel: SmsViewModel = getViewModel(
        key = "sms_app_screen",
        factory = viewModelFactory {
            SmsViewModel(smsRepository = SmsModule.smsRepository)
        }
    )

    val smsState = smsViewModel.state

    // 7599946274

    val composition = rememberKottieComposition(
        spec = KottieCompositionSpec.Url("https://lottie.host/dd09ef53-b150-4c81-a3e1-b5516e940c31/GY604Ofcp4.json")
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        speed = 1f,
        iterations = 1,
        isPlaying = smsState.status
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
                    smsViewModel.onEvent(SmsEvent.SUBMIT)
                },
                modifier = modifier
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
                    .fillMaxSize(),
            ) {

                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TextField(
                        value = smsState.recipient,
                        onValueChange = {
                            smsViewModel.onEvent(SmsEvent.RECIPIENT(it))
                        },
                        modifier = modifier.fillMaxWidth(),
                        label = {
                            Text(
                                text = "Recipient"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Divider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 3.dp
                    )

                    TextField(
                        value = smsState.body,
                        onValueChange = {
                            smsViewModel.onEvent(SmsEvent.BODY(it))
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f),
                        label = {
                            Text(
                                text = "Body"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                }

                ProgressBar(isLoading = smsState.isLoading)

                if (smsState.status) {
                    KottieAnimation(
                        composition = composition,
                        progress = { animationState.progress },
                        modifier = modifier
                            .fillMaxSize()
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
            smsViewModel.clear()
        }
    }

}