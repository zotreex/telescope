package ru.zotreex.telescope.player

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.common.util.UnstableApi
import com.github.fengdai.compose.media.Media
import com.github.fengdai.compose.media.ResizeMode
import com.github.fengdai.compose.media.ShowBuffering
import com.github.fengdai.compose.media.SurfaceType
import com.github.fengdai.compose.media.rememberMediaState
import org.koin.androidx.compose.koinViewModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(chatId: Long, messageId: Long) {

    val model: PlayerScreenModel = koinViewModel()

    model.load(chatId, messageId)
    val playerExo = model.media.collectAsState().value

    if (playerExo != null) {

        val state = rememberMediaState(player = playerExo)
        Media(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            surfaceType = SurfaceType.TextureView,
            resizeMode = ResizeMode.Fit,
            keepContentOnPlayerReset = false,
            useArtwork = true,
            showBuffering = ShowBuffering.Always,
            buffering = {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        )

    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}