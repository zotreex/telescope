package ru.zotreex.telescope.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.MessageVideo
import org.drinkless.tdlib.TdApi.MessageVideoNote
import ru.zotreex.telescope.core.TdClient
import ru.zotreex.telescope.navigation.GlobalRouter
import kotlin.coroutines.resume

class HomeScreenModel(private val tdClient: TdClient, val globalRouter: GlobalRouter) :
    ViewModel() {

    val state = MutableStateFlow<List<VideoGroup>>(emptyList())

    init {
        viewModelScope.launch {
            val getChatsRequest = TdApi.GetChats(TdApi.ChatListMain(), 100)

            tdClient.client.send(getChatsRequest) { response ->
                if (response is TdApi.Chats) {
                    chatIdsToMessaget(response.chatIds)
                }
            }

        }

    }

    private fun chatIdsToMessaget(chatIds: LongArray) {
        viewModelScope.launch {
            val chatNames = chatIds.map {
                async { getChatName(it) }
            }.awaitAll()
            val newList = chatIds.map {
                async {
                    val list = getMessagesByChatId(it)
                    /* При изначальной выборке, TDlib может вернуть 1 сообщение в целях
                     оптимизации запроса. Поэтому нужно перезапросить весь чат отдельно */
                    val firstMessages = if (list.size == 1) getMessagesByChatId(
                        it,
                        startId = list[0].id
                    ) else emptyList()
                    list + firstMessages
                }
            }.awaitAll()

            state.update {
                newList.mapIndexed { index, chat ->
                    async {
                        VideoGroup(
                            name = chatNames[index],
                            videos = chat.filter {
                                it.content is MessageVideo
                            }.map { message ->
                                async { filterMessage(message.content, message) }
                            }.awaitAll()
                        )
                    }
                }.awaitAll().filter { it.videos.isNotEmpty() }
            }
        }
    }

    private suspend fun getChatName(chatId: Long) = suspendCancellableCoroutine { continuation ->
        val getMessagesRequest = TdApi.GetChat(chatId)
        tdClient.client.send(getMessagesRequest) { response ->
            if (response is TdApi.Chat) {
                continuation.resume(response.title)
            } else {
                continuation.resume("")
            }
        }
    }

    private suspend fun filterMessage(
        content: TdApi.MessageContent,
        message: TdApi.Message
    ): VideoContent {
        if (content is TdApi.MessageVideo) {
            return VideoContent(
                content.caption.text.toString(),
                messageId = message,
                downloadFile(content.video.thumbnail?.file)
            )
        } else {
            (content as MessageVideoNote).let {
                return VideoContent(
                    content.videoNote.video.remote.id,
                    messageId = message,
                    downloadFile(content.videoNote.thumbnail?.file)
                )
            }
        }
    }

    private suspend fun getMessagesByChatId(chatId: Long, startId: Long = 0): List<TdApi.Message> =
        suspendCancellableCoroutine { continuation ->
            val getMessagesRequest = TdApi.GetChatHistory(chatId, startId, 0, 50, false)
            tdClient.client.send(getMessagesRequest) { response ->
                if (response is TdApi.Messages) {
                    continuation.resume(response.messages.toList())
                } else {
                    continuation.resume(emptyList())
                }
            }
        }

    private suspend fun downloadFile(file: TdApi.File?): String? {
        file ?: return null

        val hasfile: String? = file.local.path

        if (!hasfile.isNullOrBlank()) {
            return hasfile
        }

        return suspendCancellableCoroutine { continuation ->
            tdClient.client.send(
                TdApi.DownloadFile(file.id, 1, 0, 0, true)
            ) {
                if (it is TdApi.File) {
                    continuation.resume(it.local.path)
                } else
                    continuation.resume(null)
            }
        }
    }
}

data class VideoGroup(
    val videos: List<VideoContent>,
    val name: String
)

data class VideoContent(val text: String, val messageId: TdApi.Message, val image: String? = null)