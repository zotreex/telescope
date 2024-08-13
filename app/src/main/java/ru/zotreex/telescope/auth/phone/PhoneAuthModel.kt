package ru.zotreex.telescope.auth.phone

import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.GlobalRouter
import ru.zotreex.telescope.auth.code.CodeAuthScreen
import ru.zotreex.telescope.auth.two_factor.TwoFactorScreen
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.core.TdClient

class PhoneAuthModel(
    val authorizationHandler: AuthorizationHandler,
    val tdClient: TdClient,
    val globalRouter: GlobalRouter
) : ScreenModel {

    init {
        screenModelScope.launch {
            authorizationHandler.events.collect { event ->
                if (event is TdApi.AuthorizationStateWaitCode) {
                    globalRouter.push(CodeAuthScreen(""))
                }

                if (event is TdApi.AuthorizationStateWaitPassword) {
                    globalRouter.push(TwoFactorScreen())
                }
            }
        }
    }

    fun next() {
        screenModelScope.launch {
            tdClient.client.send(TdApi.SetAuthenticationPhoneNumber("+7" + phoneField.value, null)) {
                Log.e("PhoneAuthModel", it.toString())
            }
        }
    }

    val phoneField = MutableStateFlow("")

    //
}