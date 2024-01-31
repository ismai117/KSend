import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
internal fun App(
    modifier: Modifier = Modifier
) = MaterialTheme {


}

expect fun getPlatformName(): String
