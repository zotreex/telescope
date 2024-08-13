package ru.zotreex.telescope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.compose.TelescopeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import org.koin.android.ext.android.get
import ru.zotreex.telescope.auth.code.CodeAuthScreen
import ru.zotreex.telescope.auth.qr.QrAuthScreen
import ru.zotreex.telescope.auth.two_factor.TwoFactorScreen
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.home.HomeScreen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authState: StartRoute = get()
        val globalRouter: GlobalRouter = get()

        setContent {
            TelescopeTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {

                    val route = authState.route.collectAsState().value
                    val globalRoute = globalRouter.route.collectAsState().value

                    route?.let {
                        Navigator(it) { navigator ->
                            SlideTransition(navigator)
                            globalRoute?.let { command ->
                                when (command) {
                                    is GlobalRouter.Commands.Push -> navigator.push(command.screen)
                                    is GlobalRouter.Commands.Pop -> navigator.pop()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class GlobalRouter {

    val route = MutableStateFlow<Commands?>(null)

    fun pop() {
        route.update {
            Commands.Pop
        }
    }

    fun push(screen: Screen) {
        route.update {
            Commands.Push(screen)
        }
    }

    sealed class Commands {
        data object Pop : Commands()
        data class Push(val screen: Screen) : Commands()
    }
}


class StartRoute(val authorizationHandler: AuthorizationHandler) {

    val route = MutableStateFlow<Screen?>(null)


    init {
        CoroutineScope(Dispatchers.Main).launch {
            val state = authorizationHandler.events.first {
                it is TdApi.AuthorizationStateWaitPhoneNumber || it is TdApi.AuthorizationStateReady || it is TdApi.AuthorizationStateWaitPassword || it is TdApi.AuthorizationStateWaitOtherDeviceConfirmation
                        || it is TdApi.AuthorizationStateWaitCode
            }

            route.update {
                when (state) {
                    is TdApi.AuthorizationStateWaitPhoneNumber -> QrAuthScreen()
                    is TdApi.AuthorizationStateReady -> HomeScreen()
                    is TdApi.AuthorizationStateWaitPassword -> TwoFactorScreen()
                    is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> QrAuthScreen()
                    is TdApi.AuthorizationStateWaitCode -> CodeAuthScreen("")
                    else -> null
                }
            }
        }
    }
}

//
//class ResHand() : Client.ResultHandler {
//    override fun onResult(obj: TdApi.Object?) {
//
//        val test = obj is TdApi.UpdateAuthorizationState
//        val test2 =
//            (obj as? TdApi.UpdateAuthorizationState)?.authorizationState is AuthorizationStateWaitPhoneNumber
//        Log.e("exep", obj.toString() + test2.toString())
//        if (obj is TdApi.UpdateAuthorizationState) {
//            when (val state = obj.authorizationState) {
//                is AuthorizationStateWaitPhoneNumber -> {
//                    client.send(
//                        TdApi.RequestQrCodeAuthentication()
//                    ) {
//
//                        if (it is TdApi.Ok) {
//                            Log.e("test", "qrcode" + it.toString())
//
//                        }
//                    }
//                }
//
//                is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> {
//                    val url = state.link
//                }
//
//                is AuthorizationStateReady -> {
//
//                    client.send(TdApi.LogOut()) {
//                        if (it is TdApi.Ok) {
//                            Log.e("test", "logout" + it.toString())
//
//                        }
//                    }
//                }
//
//            }
//        }
//    }
//
//}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TelescopeTheme {
        Greeting("Android")
    }
}