package ru.zotreex.telescope.core

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.AuthorizationStateWaitTdlibParameters

class TdClient(
    appContext: Context,
    resultHandler: TdClientResultHandler,
    private val authorizationHandler: AuthorizationHandler
) {

    private val tdlibParametersss = TdApi.SetTdlibParameters().apply {
        useTestDc = false
        useChatInfoDatabase = true
        useMessageDatabase = true
        useSecretChats = false
        apiId = 45287
        apiHash = "656d433030d1900530312bb68e1117e0"
        systemLanguageCode = "ru"
        deviceModel = "Smart Tv"
        applicationVersion = "0.0.1"
        databaseDirectory = appContext.filesDir.path + "/tdlib/"
        filesDirectory = appContext.filesDir.path + "/tdlib_files/"
    }

    val client = Client.create(
        resultHandler,
        ExepHand(),
        ExepHand2()
    )

    init {
        CoroutineScope(Dispatchers.IO).launch {
            authorizationHandler.events.collect { event ->
                when (event) {
                    is AuthorizationStateWaitTdlibParameters -> {
                        client.send(tdlibParametersss) { Unit }
                    }
                }
            }
        }
    }
}

class ExepHand2() : Client.ExceptionHandler {
    override fun onException(e: Throwable?) {
        Log.e("exep", e.toString())
    }

}

class ExepHand() : Client.ExceptionHandler {
    override fun onException(e: Throwable?) {
        Log.e("exep", e.toString())
    }

}

class AuthorizationHandler() {
    val events = MutableSharedFlow<TdApi.AuthorizationState>()

    fun newEvent(state: TdApi.AuthorizationState) {
        CoroutineScope(Dispatchers.Main).launch {
            events.emit(state)
        }
    }
}

class TdClientResultHandler(private val authorizationHandler: AuthorizationHandler) :
    Client.ResultHandler {
    override fun onResult(obj: TdApi.Object?) {
        Log.e("TdApi", obj.toString())
        when (obj) {
            is TdApi.UpdateAuthorizationState -> authorizationHandler.newEvent(obj.authorizationState)
        }
    }
}