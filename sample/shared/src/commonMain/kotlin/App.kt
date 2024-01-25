import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import email.EmailScreen
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import sms.SmsScreen


@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun App(
    modifier: Modifier = Modifier
) = MaterialTheme {

    PreComposeApp {

        val navigator = rememberNavigator()

        NavHost(
            navigator = navigator,
            initialRoute = STARTER,
            navTransition = NavTransition()
        ){
            scene(
                route = STARTER
            ){
                StarterScreen(
                    navigateToEmailScreen = {
                        navigator.navigate(EMAIL)
                    },
                    navigateToSmsScreen = {
                        navigator.navigate(SMS)
                    }
                )
            }
            scene(
                route = EMAIL
            ){
                EmailScreen(
                    navigator = navigator
                )
            }
            scene(
                route = SMS
            ){
                SmsScreen(
                    navigator = navigator
                )
            }
        }

    }


}

const val STARTER = "starter_screen"
const val EMAIL = "email_screen"
const val SMS = "sms_screen"

expect fun getPlatformName(): String
