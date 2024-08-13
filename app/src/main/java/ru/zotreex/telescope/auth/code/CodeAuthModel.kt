package ru.zotreex.telescope.auth.code

import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.GlobalRouter
import ru.zotreex.telescope.auth.two_factor.TwoFactorScreen
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.core.TdClient
import ru.zotreex.telescope.home.HomeScreen

class CodeAuthModel(
    val authorizationHandler: AuthorizationHandler,
    val tdClient: TdClient,
    val globalRouter: GlobalRouter
) : ScreenModel {
    init {
        screenModelScope.launch {
            authorizationHandler.events.collect { event ->
                if (event is TdApi.AuthorizationStateWaitPassword) {
                    globalRouter.push(TwoFactorScreen())
                }

                if (event is TdApi.AuthorizationStateReady) {
                    globalRouter.push(HomeScreen())
                }
            }
        }
    }

    fun onNext() {
        screenModelScope.launch {
            tdClient.client.send(TdApi.CheckAuthenticationCode(codeField.value)) {
                Log.e("CodeAuthModel", it.toString())

                /*
                *  code = 400
                   message = "PHONE_NUMBER_INVALID"
                * */
            }
        }
    }

    val codeField = MutableStateFlow("")

    //.send(TdApi.CheckAuthenticationCode(it.text.toString()))
}