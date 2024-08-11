package ru.zotreex.telescope.experenets

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parametersOf

@Composable
fun App() {
    Navigator(DataScreen())
}

class DataScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val model = navigator.getNavigatorScreenModel<DataScreenModel>(parameters = { parametersOf("idds") })


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