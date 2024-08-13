package ru.zotreex.telescope.player

import android.app.Application
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.Message
import ru.zotreex.telescope.core.TdClient
import kotlin.coroutines.resume

class PlayerScreenModel(private val tdClient: TdClient, app: Application) : ScreenModel {

    val media = MutableStateFlow<String?>(null)
    var flag: Boolean = false

    val player = ExoPlayer.Builder(app)
        .build()

    init {

        player.playWhenReady = true
        player.prepare()
    }

    fun load(message: Message) {
        if (flag) return
        flag = true
        screenModelScope.launch {
            (message.content as? TdApi.MessageVideo)?.let { content ->

                val uri = handleVideo(content)
                uri?.let { player.setMediaItem(MediaItem.fromUri(uri)) }

                media.update {
                    uri
                }
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        player.release()
    }

    private suspend fun handleVideo(content: TdApi.MessageVideo): String? {
        val file = content.video.video
        val hasfile: String? = file.local.path

        if (!hasfile.isNullOrBlank()) {
            return hasfile
        } else {

        }

        return suspendCancellableCoroutine { continuation ->
            tdClient.client.send(TdApi.DownloadFile(file.id, 1, 0, 0, true), {
                if (it is TdApi.File) {
                    continuation.resume(it.local.path)
                } else
                    continuation.resume(null)
            }
            )
        }
    }
}