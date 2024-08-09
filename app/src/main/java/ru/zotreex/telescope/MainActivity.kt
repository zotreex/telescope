package ru.zotreex.telescope

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import ru.zotreex.telescope.ui.theme.TelescopeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TelescopeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    Greeting("Android")
                }
            }
        }

        val TdlibParameters = TdApi.TdlibParameters().apply {
            useTestDc = true
            useChatInfoDatabase = true
            useMessageDatabase = true
            useSecretChats = false
            apiId = 45287
            apiHash = "656d433030d1900530312bb68e1117e0"
            systemLanguageCode = "ru"
            deviceModel = "Smart Tv"
            applicationVersion = "0.0.1"
            enableStorageOptimizer = true
            databaseDirectory = baseContext.getFilesDir().absolutePath + "/tdlib/"
            filesDirectory = baseContext.getFilesDir().absolutePath + "/tdlib_files/"
        }

        val client = Client.create({ Log.e("client", it.toString()) }, null, null)

        client.send(TdApi.SetTdlibParameters(TdlibParameters)) {
            Log.e("test", it.toString())
            if (it is TdApi.Ok) {
                client.send(TdApi.CheckDatabaseEncryptionKey()) {
                    Log.e("test", it.toString())
                    client.send(TdApi.SetAuthenticationPhoneNumber("79823683779", null)) {
                        Log.e("test", it.toString())
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