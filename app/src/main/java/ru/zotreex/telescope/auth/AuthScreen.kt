package ru.zotreex.telescope.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.compose.TelescopeTheme
import ru.zotreex.telescope.core.qr.QrCodeImage

class AuthScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AuthContent()
    }
}

@Composable
private fun AuthContent() {

    Surface(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight()
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.width(650.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Card(
                            modifier = Modifier.size(200.dp),
                            shape = MaterialTheme.shapes.large,
                            colors = CardColors(
                                containerColor = MaterialTheme.colorScheme.inverseSurface,
                                Color.Unspecified,
                                Color.Unspecified,
                                Color.Unspecified,
                            )
                        ) {
                            QrCodeImage(
                                content = "https://google.com/",
                                size = 200.dp,
                                modifier = Modifier.padding(10.dp),
                                backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                                dotsColor = MaterialTheme.colorScheme.surface
                            )
                        }

                    }

                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp)
                    ) {
                        Text(
                            text = "Быстрый вход по QR коду",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            StepText(step = "1", text = "Откройте Telegram с телефона")
                            StepText(
                                step = "2",
                                text = "Перейдите в Настройки > Устройства > Подключить устройство"
                            )
                            StepText(
                                step = "3",
                                text = "Для подтверждения направьте камеру телефона на этот экран"
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedButton(onClick = { }) {
                            Text(text = "ВХОД ПО НОМЕРУ ТЕЛЕФОНА")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepText(step: String, text: String) {
    Row {
        Badge(
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 10.dp)
        ) {
            Text(text = step, color = Color.White)
        }
        Text(text = text)
    }
}

@Preview(
    device = "spec:parent=tv_1080p",
    name = "Dark"
)

@Composable
private fun PreviewAuthContent() {
    TelescopeTheme {
        AuthContent()
    }
}