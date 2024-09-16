package ru.zotreex.telescope.auth.two_factor

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.core.TdClient
import ru.zotreex.telescope.navigation.GlobalRouter

class TwoFactorModel(
    private val tdClient: TdClient,
    val globalRouter: GlobalRouter
) : ViewModel() {

    val passwordField = MutableStateFlow("")

    fun next() {
        tdClient.client.send(TdApi.CheckAuthenticationPassword(passwordField.value)) {
            if (it is TdApi.Ok) {
                globalRouter.push("HomeScreen")
            }
        }
    }
}