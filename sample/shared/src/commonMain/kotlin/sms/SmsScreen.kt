package sms

import KSend.sample.shared.BuildConfig
import ProgressBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import moe.tlaster.precompose.navigation.Navigator
import sms.factory.SmsRepositoryFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmsScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val smsViewModel: SmsViewModel = getViewModel(
        key = "sms_app_screen",
        factory = viewModelFactory {
            SmsViewModel(smsRepository = SmsRepositoryFactory())
        }
    )

    val smsState = smsViewModel.state

    LaunchedEffect(
        key1 = smsState.error,
    ) {
        if (smsState.error.isNotBlank()){
            snackbarHostState.showSnackbar(smsState.error)
        }
    }

    var to by remember { mutableStateOf("") }
    var body  by remember { mutableStateOf("") }

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
                .fillMaxSize(),
        ) {



            Column(
                modifier = modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                TextField(
                    value = to,
                    onValueChange = {
                        to = it
                    },
                    label = {
                        Text(
                            text = "Recipient"
                        )
                    }
                )

                TextField(
                    value = body,
                    onValueChange = {
                        body = it
                    },
                    label = {
                        Text(
                            text = "Body"
                        )
                    }
                )

                Button(
                    onClick = {
                        smsViewModel.sendSms(
                            accountSID = BuildConfig.ACCOUNTSID,
                            authToken = BuildConfig.AUTHTOKEN,
                            to = to,
                            from = BuildConfig.SENDER_PHONE_NUMBER,
                            body = body
                        )
                    }
                ) {
                    Text(
                        text = "Send"
                    )
                }

            }

            ProgressBar(isLoading = smsState.isLoading)

        }

    }


}