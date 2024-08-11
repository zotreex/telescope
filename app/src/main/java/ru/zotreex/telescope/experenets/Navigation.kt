package ru.zotreex.telescope.experenets

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import org.koin.core.parameter.parametersOf
import ru.zotreex.telescope.auth.qr.QrAuthScreen

@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun App() {
    Navigator(QrAuthScreen()) { navigator ->
        SlideTransition(navigator = navigator)
    }
}

class DataScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val model =
            navigator.koinNavigatorScreenModel<DataScreenModel>(parameters = { parametersOf("idds") })


        Box {
            Text(text = model.id)
        }
        // on a button click or event call this :
        //navigator.push(DataDetailScreen())
    }
}

class DataDetailScreen : Screen {
    @Composable
    override fun Content() {
        // display data on composable
        // on a button click or event call this :
        val navigator = LocalNavigator.currentOrThrow

        //navigator.pop()
    }
}