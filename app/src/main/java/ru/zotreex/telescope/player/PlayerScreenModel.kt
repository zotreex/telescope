package ru.zotreex.telescope.player

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.core.TdClient
import kotlin.coroutines.resume

class PlayerScreenModel(private val tdClient: TdClient,private val app: Application) : ViewModel() {

    val media = MutableStateFlow<ExoPlayer?>(null)
    var flag: Boolean = false

    override fun onCleared() {
        super.onCleared()
        media.value?.release()
        media.value = null
    }

    fun load(chatId: Long, messageId: Long) {
        if (flag) return
        flag = true

        viewModelScope.launch {
            val message = getMessage(chatId, messageId)
            (message?.content as? TdApi.MessageVideo)?.let { content ->

                val uri = handleVideo(content)
                val player = ExoPlayer.Builder(app).build().apply {
                    prepare()
                    playWhenReady = true
                    addAnalyticsListener(EventLogger())
                }
                player.setMediaItem(MediaItem.fromUri(uri!!))

                media.update {
                    player
                }
            }
        }
    }

    suspend fun getMessage(chatId: Long, messageId: Long) =
        suspendCancellableCoroutine { continuation ->
            tdClient.client.send(TdApi.GetMessage(chatId, messageId)) {
                if (it is TdApi.Message) {
                    continuation.resume(it)
                } else
                    continuation.resume(null)
            }
        }

    private suspend fun handleVideo(content: TdApi.MessageVideo): String? {
        val file = content.video.video
        val hasfile: String? = file.local.path

        if (!hasfile.isNullOrBlank()) {
            return hasfile
        } else {

        }

        return suspendCancellableCoroutine { continuation ->
            tdClient.client.send(TdApi.DownloadFile(file.id, 1, 0, 0, true)
            ) {
                if (it is TdApi.File) {
                    continuation.resume(it.local.path)
                } else
                    continuation.resume(null)
            }
        }
    }
}