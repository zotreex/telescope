package ru.zotreex.telescope.auth.qr

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.GlobalRouter
import ru.zotreex.telescope.auth.two_factor.TwoFactorScreen
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.core.TdClient

class QrAuthScreenModel(
    authorizationHandler: AuthorizationHandler,
    val tdClient: TdClient,
    globalRouter: GlobalRouter
) :
    ScreenModel {


    val state = MutableStateFlow<QrAuthScreenState>(QrAuthScreenState())

    init {
        tdClient.client.send(TdApi.RequestQrCodeAuthentication()) {}
        screenModelScope.launch {
            authorizationHandler.events.collect { event ->
                if (event is TdApi.AuthorizationStateWaitOtherDeviceConfirmation) {
                    state.update {
                        QrAuthScreenState(url = event.link, qrLoading = false)
                    }
                }

                if (event is TdApi.AuthorizationStateWaitPassword) {
                    globalRouter.push(TwoFactorScreen())
                }
            }
        }
    }
}

data class QrAuthScreenState(val url: String? = null, val qrLoading: Boolean = true)