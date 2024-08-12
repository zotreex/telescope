package ru.zotreex.telescope.home

import androidx.compose.runtime.Composable
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import cafe.adriel.voyager.core.screen.Screen

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        Surface {
            Text(text = "Home screen", style = MaterialTheme.typography.headlineLarge)
        }
    }
}