import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator
import navigation.BottomNavigation
import navigation.RootNavigation


@Composable
internal fun App(
    modifier: Modifier = Modifier
) = MaterialTheme {

    PreComposeApp {

        val navigator = rememberNavigator()

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    navigator = navigator
                )
            }
        ){ paddingValues ->
            Box(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ){
                RootNavigation(
                    navigator = navigator
                )
            }
        }

    }


}

expect fun getPlatformName(): String
