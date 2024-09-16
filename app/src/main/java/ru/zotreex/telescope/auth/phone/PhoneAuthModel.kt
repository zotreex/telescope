package ru.zotreex.telescope.auth.phone

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.navigation.GlobalRouter
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.core.TdClient

class PhoneAuthModel(
    private val authorizationHandler: AuthorizationHandler,
    private val tdClient: TdClient,
    val globalRouter: GlobalRouter
) : ViewModel() {

    val phoneField = MutableStateFlow("")

    init {
        viewModelScope.launch {
            authorizationHandler.events.collect { event ->
                if (event is TdApi.AuthorizationStateWaitCode) {
                    globalRouter.push("CodeAuthScreen")
                }

                if (event is TdApi.AuthorizationStateWaitPassword) {
                    globalRouter.push("TwoFactorScreen")
                }
            }
        }
    }

    fun next() {
        viewModelScope.launch {
            tdClient.client.send(
                TdApi.SetAuthenticationPhoneNumber(
                    "+7" + phoneField.value,
                    null
                )
            ) {}
        }
    }
}