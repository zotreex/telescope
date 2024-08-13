package ru.zotreex.telescope.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.Icon
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.compose.TelescopeTheme
import org.drinkless.tdlib.TdApi
import ru.zotreex.telescope.player.PlayerScreen
import java.io.File

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        val model = nav.koinNavigatorScreenModel<HomeScreenModel>()

        val state = model.state.collectAsState().value

        Surface(modifier = Modifier.fillMaxSize()) {
            ModalNavigationDrawer(
                drawerContent = {
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .selectableGroup(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items.forEachIndexed { index, s ->
                            NavigationDrawerItem(
                                selected = index == 0,
                                onClick = { },
                                leadingContent = {
                                    Icon(
                                        painter = rememberVectorPainter(image = Icons.Default.Home),
                                        contentDescription = ""
                                    )
                                }
                            ) {
                                Text(s)
                            }
                        }
                    }
                },
                content = {
                    HomeContent(state)
                }
            )
        }
    }
}

@Composable
private fun HomeContent(state: List<VideoGroup>) {
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
                        FoundationsGridCard(item.text, item.image, item.messageId)
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
    modifier: Modifier = Modifier
) {
    val nav = LocalNavigator.currentOrThrow
    val onClick = { nav.push(PlayerScreen(message)) }

    StandardCardContainer(
        modifier = modifier,
        imageCard = {
            Card(onClick = onClick) {
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
        HomeContent(listOf())
    }
}