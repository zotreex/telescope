package ru.zotreex.telescope

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.example.compose.TelescopeTheme
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.AuthorizationStateReady
import org.drinkless.tdlib.TdApi.AuthorizationStateWaitPhoneNumber
import org.drinkless.tdlib.TdApi.AuthorizationStateWaitTdlibParameters
import ru.zotreex.telescope.Core.TdlibParametersss
import ru.zotreex.telescope.Core.client
import ru.zotreex.telescope.experenets.App

object Core {

    val TdlibParametersss = TdApi.SetTdlibParameters().apply {
        useTestDc = false
        useChatInfoDatabase = true
        useMessageDatabase = true
        useSecretChats = false
        apiId = 45287
        apiHash = "656d433030d1900530312bb68e1117e0"
        systemLanguageCode = "ru"
        deviceModel = "Smart Tv"
        applicationVersion = "0.0.1"
        databaseDirectory = "/data/user/0/ru.zotreex.telescope/files/tdlib/"
        filesDirectory =  "/data/user/0/ru.zotreex.telescope/files/tdlib_files/"
    }

    val client = Client.create(
        ResHand(),
        ExepHand(),
        ExepHand2()
    )


    fun init(baseContext: Context) {

    }
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val tmp = Core.init(this.baseContext)
        setContent {
            TelescopeTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {

                    App()

                    return@Surface
                    Column {

                        Greeting("Android")
                        val textState = remember { mutableStateOf(TextFieldValue()) }

                        OutlinedTextField(
                            value = textState.value,
                            onValueChange = {
                                textState.value = it
                                if (it.text.length == 5) {
                                    Core.client.send(TdApi.CheckAuthenticationCode(it.text.toString())) {
                                        Log.e("test", it.toString())
                                        if (it is TdApi.Ok) {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Code OK",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                        )

                        Button(onClick = {
                            client.send(TdApi.SetAuthenticationPhoneNumber("+79956790671", null)) {
                                Log.e("test", "test phone ")
                                if (it is TdApi.Ok) {
                                    Log.e("test", "test phone ok")
                                }}
                        }){
                            Text("Click", fontSize = 25.sp)
                        }
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

class ResHand() : Client.ResultHandler {
    override fun onResult(obj: TdApi.Object?) {

        val test = obj is TdApi.UpdateAuthorizationState
        val test2 = (obj as? TdApi.UpdateAuthorizationState)?.authorizationState is AuthorizationStateWaitPhoneNumber
        Log.e("exep", obj.toString() + test2.toString())
        if (obj is TdApi.UpdateAuthorizationState) {
            when (val state = obj.authorizationState) {
                is AuthorizationStateWaitPhoneNumber -> {
                    client.send(
                        TdApi.RequestQrCodeAuthentication()
                    ) {

                        if (it is TdApi.Ok) {
                            Log.e("test", "qrcode" + it.toString())

                        }
                    }
                }

                is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> {
                    val url = state.link
                }

                is AuthorizationStateReady -> {

                    client.send(TdApi.LogOut()) {
                        if (it is TdApi.Ok) {
                            Log.e("test", "logout" + it.toString())

                        }
                    }
                }

                is AuthorizationStateWaitTdlibParameters -> {
                    Log.e("test", "lib send ${TdlibParametersss.apiHash}")
                    client.send(
                        TdlibParametersss
                    ) {

                        if (it is TdApi.Ok) {
                            Log.e("test", "lib params appluied")

                        }
                    }
                }
            }
        }
    }

}

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