package ru.zotreex.telescope.navigation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.core.AuthorizationHandler

class StartRoute(private val authorizationHandler: AuthorizationHandler) {

    val route = MutableStateFlow<String?>(null)


    init {
        CoroutineScope(Dispatchers.Main).launch {
            val state = authorizationHandler.events.first {
                it is TdApi.AuthorizationStateWaitPhoneNumber || it is TdApi.AuthorizationStateReady
                        || it is TdApi.AuthorizationStateWaitPassword
                        || it is TdApi.AuthorizationStateWaitOtherDeviceConfirmation
                        || it is TdApi.AuthorizationStateWaitCode
            }

            route.update {
                when (state) {
                    is TdApi.AuthorizationStateWaitPhoneNumber -> "QrAuthScreen"
                    is TdApi.AuthorizationStateReady -> "home"
                    is TdApi.AuthorizationStateWaitPassword -> "TwoFactorScreen"
                    is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> "QrAuthScreen"
                    is TdApi.AuthorizationStateWaitCode -> "CodeAuthScreen"
                    else -> null
                }
            }
        }
    }
}