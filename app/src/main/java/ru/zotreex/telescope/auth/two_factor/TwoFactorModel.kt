package ru.zotreex.telescope.auth.two_factor

import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.GlobalRouter
import ru.zotreex.telescope.core.TdClient
import ru.zotreex.telescope.home.HomeScreen

class TwoFactorModel(
    val tdClient: TdClient,
    val globalRouter: GlobalRouter
) : ScreenModel {
    fun next() {
        tdClient.client.send(TdApi.CheckAuthenticationPassword(passwordField.value)) {
            Log.e("TwoFactorModel", it.toString())
            if (it is TdApi.Ok) {
                globalRouter.push(HomeScreen())
            }
        }
    }

    val passwordField = MutableStateFlow("")
}