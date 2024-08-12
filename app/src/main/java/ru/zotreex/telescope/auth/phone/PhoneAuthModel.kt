package ru.zotreex.telescope.auth.phone

import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.core.TdClient

class PhoneAuthModel(val tdClient: TdClient) : ScreenModel {
    fun next(s: String) {
        screenModelScope.launch {
            tdClient.client.send(TdApi.SetAuthenticationPhoneNumber("+7"+ s, null)) {
                Log.e("PhoneAuthModel", it.toString())
            }
        }
    }

    val phoneField = MutableStateFlow("")

    //
}