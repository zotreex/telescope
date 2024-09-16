package ru.zotreex.telescope.auth.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.core.TdClient
import ru.zotreex.telescope.navigation.GlobalRouter

class CodeAuthModel(
    private val authorizationHandler: AuthorizationHandler,
    private val tdClient: TdClient,
    val globalRouter: GlobalRouter
) : ViewModel() {

    val codeField = MutableStateFlow("")

    init {
        viewModelScope.launch {
            authorizationHandler.events.collect { event ->
                if (event is TdApi.AuthorizationStateWaitPassword) {
                    globalRouter.push("TwoFactorScreen")
                }

                if (event is TdApi.AuthorizationStateReady) {
                    globalRouter.push("HomeScreen")
                }
            }
        }
    }

    fun onNext() {
        viewModelScope.launch {
            tdClient.client.send(TdApi.CheckAuthenticationCode(codeField.value)) {}
        }
    }
}