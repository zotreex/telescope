package ru.zotreex.telescope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.Surface
import com.example.compose.TelescopeTheme
import org.koin.android.ext.android.get
import ru.zotreex.telescope.auth.code.CodeAuthScreen
import ru.zotreex.telescope.auth.phone.PhoneAuthScreen
import ru.zotreex.telescope.auth.qr.QrAuthScreen
import ru.zotreex.telescope.auth.two_factor.TwoFactorScreen
import ru.zotreex.telescope.home.HomeScreen
import ru.zotreex.telescope.navigation.GlobalRouter
import ru.zotreex.telescope.navigation.StartRoute
import ru.zotreex.telescope.player.PlayerScreen

class MainActivity : ComponentActivity() {

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

                    val startRoute = authState.route.collectAsState()

                    startRoute.value?.let {
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = it) {
                            composable("home") { HomeScreen() }
                            composable("HomeScreen") { HomeScreen() }
                            composable("QrAuthScreen") { QrAuthScreen() }
                            composable("TwoFactorScreen") { TwoFactorScreen() }
                            composable("QrAuthScreen") { QrAuthScreen() }
                            composable("CodeAuthScreen") { CodeAuthScreen("") }
                            composable("PhoneAuthScreen") { PhoneAuthScreen() }
                            composable(
                                route = "player/{chatId}/{messageId}",
                                arguments = listOf(
                                    navArgument("chatId") { type = NavType.LongType },
                                    navArgument("messageId") { type = NavType.LongType }
                                )
                            ) { backStackEntry ->
                                val chatId = backStackEntry.arguments?.getLong("chatId") ?: 0
                                val messageId = backStackEntry.arguments?.getLong("messageId") ?: 0
                                PlayerScreen(chatId = chatId, messageId = messageId)
                            }
                        }


                        val globalRoute = globalRouter.route.collectAsState().value

                        globalRoute?.let { command ->
                            when (command) {
                                is GlobalRouter.Commands.Push -> navController.navigate(command.screen)
                                is GlobalRouter.Commands.Pop -> navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}