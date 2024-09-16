package ru.zotreex.telescope.auth.qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.navigation.GlobalRouter
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.core.TdClient

class QrAuthScreenModel(
    private val authorizationHandler: AuthorizationHandler,
    private val tdClient: TdClient,
    val globalRouter: GlobalRouter
) : ViewModel() {

    val state = MutableStateFlow<QrAuthScreenState>(QrAuthScreenState())

    init {
        tdClient.client.send(TdApi.RequestQrCodeAuthentication()) {}
        viewModelScope.launch {
            authorizationHandler.events.collect { event ->
                if (event is TdApi.AuthorizationStateWaitOtherDeviceConfirmation) {
                    state.update {
                        QrAuthScreenState(url = event.link, qrLoading = false)
                    }
                }

                if (event is TdApi.AuthorizationStateWaitPassword) {
                    globalRouter.push("TwoFactorScreen")
                }

                if (event is TdApi.AuthorizationStateReady) {
                    globalRouter.push("HomeScreen")
                }
            }
        }
    }

    fun onPhoneClick() {
        tdClient.client.send(TdApi.LogOut()) {

            viewModelScope.launch {

                val closed =
                    authorizationHandler.events.first { it is TdApi.AuthorizationStateClosed }

                tdClient.refreshClient()
                val waitPhone =
                    authorizationHandler.events.first { it is TdApi.AuthorizationStateWaitPhoneNumber }
                globalRouter.push("PhoneAuthScreen")
            }
        }
    }

}

data class QrAuthScreenState(val url: String? = null, val qrLoading: Boolean = true)