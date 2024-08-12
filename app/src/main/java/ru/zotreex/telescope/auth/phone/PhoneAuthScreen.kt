package ru.zotreex.telescope.auth.phone

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.compose.TelescopeTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.zotreex.telescope.auth.code.CodeAuthScreen
import ru.zotreex.telescope.core.fields.MaskVisualTransformation
import ru.zotreex.telescope.core.fields.NumberMask
import ru.zotreex.telescope.core.fields.NumberMask.phoneSize

@OptIn(ExperimentalVoyagerApi::class)
class PhoneAuthScreen : Screen {
    @Composable
    override fun Content() {

        val nav = LocalNavigator.currentOrThrow
        val model = koinScreenModel<PhoneAuthModel>()

        PhoneContent(
            phoneField = model.phoneField,
            onBackClick = { nav.pop() },
            onNextClick = model::next
        )
    }
}


@Composable
private fun PhoneContent(
    phoneField: MutableStateFlow<String>,
    onNextClick: (phone: String) -> Unit,
    onBackClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Войти в Telegram",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Проверьте свой номер телефона.",
                    style = MaterialTheme.typography.bodyMedium
                )

                val requester = remember { FocusRequester() }
                val textState by phoneField.collectAsState()
                OutlinedTextField(
                    modifier = Modifier
                        .fillColumns(4)
                        .focusRequester(requester),
                    value = textState,
                    onValueChange = { text ->
                        if (text.length <= phoneSize) {
                            phoneField.update { text.filter { it.isDigit() } }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = MaskVisualTransformation(NumberMask.PHONE),
                    label = { Text(text = "Номер телефона") },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            onNextClick(textState)
                        }
                    )
                )

                SideEffect {
                    requester.requestFocus()
                }

                Button(
                    onClick = { onNextClick(textState) },
                    modifier = Modifier
                        .fillColumns(4)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Next",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }

                OutlinedButton(
                    onClick = { onBackClick() },
                    modifier = Modifier.fillColumns(4)
                )
                {
                    Text(
                        text = "Назад",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Preview(
    device = "spec:parent=tv_1080p",
    name = "Dark"
)

@Composable
private fun PreviewAuthContent() {
    TelescopeTheme {
        PhoneContent(MutableStateFlow(""), {}, {})
    }
}

@Stable
fun Modifier.fillColumns(
    @IntRange(from = 1, to = 12)
    columnsCount: Int
): Modifier {
    val widthColumns = 52 * columnsCount
    val spaceBetween = 20 * (columnsCount - 1)
    return this.width(
        (widthColumns + spaceBetween).dp
    )
}