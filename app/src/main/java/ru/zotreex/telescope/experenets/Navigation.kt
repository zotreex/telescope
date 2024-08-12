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
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf
import ru.zotreex.telescope.StartRoute
import ru.zotreex.telescope.auth.qr.QrAuthScreen

@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun App(screen: Screen) {

}