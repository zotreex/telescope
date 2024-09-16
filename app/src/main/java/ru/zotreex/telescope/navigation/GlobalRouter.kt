package ru.zotreex.telescope.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class GlobalRouter {

    val route = MutableStateFlow<Commands?>(null)

    fun pop() {
        route.update {
            Commands.Pop
        }
    }

    fun push(screen: String) {
        route.update {
            Commands.Push(screen)
        }
    }

    sealed class Commands {
        data object Pop : Commands()
        data class Push(val screen: String) : Commands()
    }
}