import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun StarterScreen(
    modifier: Modifier = Modifier,
    navigateToEmailScreen: () -> Unit,
    navigateToSmsScreen: () -> Unit
){
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column {
            TextButton(
                onClick = {
                    navigateToEmailScreen()
                }
            ){
                Text(
                    text = "Email"
                )
            }
            TextButton(
                onClick = {
                    navigateToSmsScreen()
                }
            ){
                Text(
                    text = "Sms"
                )
            }
        }
    }
}