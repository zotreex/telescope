package ru.zotreex.telescope.auth.qr

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.zotreex.telescope.auth.phone.PhoneAuthScreen

class QrAuthScreenModel : ScreenModel {

    val state = MutableStateFlow<AuthScreenState>(AuthScreenState.Qr(false))

    fun back() {

    }
}

sealed class AuthScreenState {
    data class Qr(val qrLoading: Boolean) : AuthScreenState()

    data class Phone(val tmp: String) : AuthScreenState()

    data class Code(val tmp: String) : AuthScreenState()

    data class Password(val tmp: String) : AuthScreenState()
}