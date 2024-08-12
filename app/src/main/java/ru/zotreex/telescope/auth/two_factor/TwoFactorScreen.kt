package ru.zotreex.telescope.auth.two_factor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.compose.TelescopeTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.zotreex.telescope.auth.phone.fillColumns
import ru.zotreex.telescope.core.fields.MaskVisualTransformation
import ru.zotreex.telescope.core.fields.NumberMask
import ru.zotreex.telescope.core.fields.NumberMask.phoneSize

class TwoFactorScreen : Screen {
    @Composable
    override fun Content() {

        val nav = LocalNavigator.currentOrThrow
        val model = koinScreenModel<TwoFactorModel>()

        TwoFactorAuthContent(model.passwordField, model::next, { nav.pop() })
    }
}

@Composable
private fun TwoFactorAuthContent(
    codeField: MutableStateFlow<String>,
    onNextClick: () -> Unit,
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
                    text = "Введите пароль",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Вы включили двухэтапную аутентификацию.\n Ваш аккаунт защищён дополнительным\nоблачным паролем.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                val requester = remember { FocusRequester() }
                val textState by codeField.collectAsState()
                OutlinedTextField(
                    modifier = Modifier
                        .fillColumns(4)
                        .focusRequester(requester),
                    value = textState,
                    onValueChange = { text ->
                        if (text.length <= phoneSize) {
                            codeField.update { text }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    label = { Text(text = "Пароль") },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            onNextClick()
                        }
                    ),
                    supportingText = {
                        Text(
                            text = "Подсказка",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )

                SideEffect {
                    requester.requestFocus()
                }

                Button(
                    onClick = { onNextClick() },
                    modifier = Modifier
                        .fillColumns(4)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Продолжить",
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
                        text = "Изменить номер",
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
private fun PreviewTwoFactorContent() {
    TelescopeTheme {
        TwoFactorAuthContent(MutableStateFlow(""), {}, {})
    }
}