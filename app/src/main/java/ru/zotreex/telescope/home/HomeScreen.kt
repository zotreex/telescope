package ru.zotreex.telescope.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.rememberAsyncImagePainter
import com.example.compose.TelescopeTheme
import org.drinkless.tdlib.TdApi
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun HomeScreen() {

    val model: HomeScreenModel = koinViewModel()

    val state = model.state.collectAsState().value

    Surface(modifier = Modifier.fillMaxSize()) {
        if (state.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            HomeContent(state) { chatId, messageId ->
                model.globalRouter.push("player/$chatId/$messageId")
            }
        }
    }
}

@Composable
private fun HomeContent(state: List<VideoGroup>, onClick: (Long, Long) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .weight(1f)
                .padding(start = 54.dp, top = 0.dp, end = 38.dp, bottom = 12.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.padding(top = 12.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                state.forEachIndexed { index, videoGroup ->
                    item(span = { GridItemSpan(4) }) {
                        Text(text = videoGroup.name)
                    }

                    itemsIndexed(videoGroup.videos) { index, item ->
                        FoundationsGridCard(item.text, item.image, item.messageId, onClick)
                    }
                }
            }
        }
    }
}

@Composable
fun FoundationsGridCard(
    foundation: String,
    item: String?,
    message: TdApi.Message,
    onClick: (Long, Long) -> Unit,
    modifier: Modifier = Modifier
) {


    StandardCardContainer(
        modifier = modifier,
        imageCard = {
            Card(onClick = { onClick(message.chatId, message.id) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            Color.Blue
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val painter = rememberAsyncImagePainter(model = item?.let { it1 -> File(it1) })
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        painter = painter,
                        contentDescription = null
                    )
                }
            }
        },
        title = {
            Text(
                maxLines = 2,
                text = foundation,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    )
}

val items = listOf("asadasd", "bsadasdas", "sadasdc", "drfdsfsdgf", "easdawdas")

@Composable
@Preview(
    device = "spec:parent=tv_1080p",
    name = "Dark"
)
private fun HomeContentPreview() {
    TelescopeTheme {
        HomeContent(listOf(), { _, _ -> })
    }
}