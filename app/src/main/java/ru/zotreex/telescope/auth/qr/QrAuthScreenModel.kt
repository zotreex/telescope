package ru.zotreex.telescope.auth.qr

import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.GlobalRouter
import ru.zotreex.telescope.auth.phone.PhoneAuthScreen
import ru.zotreex.telescope.auth.two_factor.TwoFactorScreen
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.core.TdClient
import ru.zotreex.telescope.home.HomeScreen

class QrAuthScreenModel(
    val authorizationHandler: AuthorizationHandler,
    val tdClient: TdClient,
    val globalRouter: GlobalRouter
) :
    ScreenModel {
    fun onPhoneClick() {
        tdClient.client.send(TdApi.LogOut()) {

            screenModelScope.launch {

                val closed =
                    authorizationHandler.events.first { it is TdApi.AuthorizationStateClosed }

                tdClient.refreshClient()
                val waitPhone =
                    authorizationHandler.events.first { it is TdApi.AuthorizationStateWaitPhoneNumber }
                globalRouter.push(PhoneAuthScreen())
            }
        }
    }


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

                if (event is TdApi.AuthorizationStateReady) {
                    globalRouter.push(HomeScreen())
                }
            }
        }
    }
}

data class QrAuthScreenState(val url: String? = null, val qrLoading: Boolean = true)