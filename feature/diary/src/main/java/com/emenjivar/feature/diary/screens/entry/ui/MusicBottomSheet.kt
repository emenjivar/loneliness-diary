package com.emenjivar.feature.diary.screens.entry.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.emenjivar.core.data.models.Mocks
import com.emenjivar.core.data.models.SongModel
import com.emenjivar.core.data.utils.ResultWrapper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicBottomSheet(
    sheetState: SheetState,
    songs: ResultWrapper<List<SongModel>>,
    recentSongs: List<SongModel>,
    search: String,
    modifier: Modifier = Modifier,
    onSearchSong: (String) -> Unit,
    onTriggerImmediateSearch: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    if (sheetState.isVisible) {
        ModalBottomSheet(
            modifier = Modifier.statusBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                coroutineScope.launch { sheetState.hide() }
            }
        ) {
            MusicBottomSheetLayout(
                songs = songs,
                recentSongs = recentSongs,
                search = search,
                modifier = modifier.fillMaxSize(),
                onSearchSong = onSearchSong,
                onTriggerImmediateSearch = onTriggerImmediateSearch
            )
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
@Stable
private fun MusicBottomSheetLayout(
    songs: ResultWrapper<List<SongModel>>,
    recentSongs: List<SongModel>,
    search: String,
    modifier: Modifier = Modifier,
    onSearchSong: (String) -> Unit,
    onTriggerImmediateSearch: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentMediaItemIndex by remember { mutableIntStateOf(-1) }

    // TODO: move to a separate hilt module
    val context = LocalContext.current
    val exoplayer = remember {
        ExoPlayer.Builder(context).build().apply {
            pauseAtEndOfMediaItems = true

            addListener(
                object : Player.Listener {
                    override fun onIsPlayingChanged(playing: Boolean) {
                        isPlaying = playing
                    }

                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        currentMediaItemIndex = this@apply.currentMediaItemIndex
                    }
                }
            )
        }
    }

    LaunchedEffect(recentSongs, songs) {
        val searchResult = (songs as? ResultWrapper.Success)?.data.orEmpty()
        val allSongs = recentSongs + searchResult
        exoplayer.apply {
            pause()
            val mediaItems = allSongs.map { song ->
                MediaItem.fromUri(song.previewUrl)
            }
            clearMediaItems()
            setMediaItems(mediaItems)
            repeatMode = Player.REPEAT_MODE_OFF
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoplayer.release() }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = search,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(text = "Search a song")
            },
//            leadingIcon = {
//                IconButton(onClick = onTriggerImmediateSearch) {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = "Search a song by title"
//                    )
//                }
//            },
            trailingIcon = {
                IconButton(onClick = { onSearchSong("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close bottomSheet"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onTriggerImmediateSearch()
                }
            ),
            onValueChange = onSearchSong
        )

        LazyColumn(
            modifier = Modifier
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (recentSongs.isNotEmpty()) {
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = "Recent songs"
                    )
                }

                itemsIndexed(recentSongs) { index, song ->
                    val isPlaying = index == currentMediaItemIndex && isPlaying
                    SongItem(
                        song = song,
                        isPlaying = isPlaying,
                        onPlay = {
                            if (isPlaying) {
                                exoplayer.pause()
                            } else {
                                exoplayer.seekTo(index, 0)
                                exoplayer.play()
                            }
                        },
                        onClick = {
                            // TODO: add here logic for inserting song in text entry
                        }
                    )
                }
            }

            when (songs) {
                is ResultWrapper.Loading -> {
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = "Searching..."
                        )
                    }
                }

                is ResultWrapper.Success -> {
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = "Results:"
                        )
                    }
                    itemsIndexed(songs.data) { index, song ->
                        val isPlaying =
                            (index + recentSongs.size) == currentMediaItemIndex && isPlaying
                        SongItem(
                            song = song,
                            isPlaying = isPlaying,
                            onPlay = {
                                if (isPlaying) {
                                    exoplayer.pause()
                                } else {
                                    exoplayer.seekTo(index, 0)
                                    exoplayer.play()
                                }
                            },
                            onClick = {
                                // TODO: add here logic for inserting song in text entry
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Stable
private fun SongItem(
    song: SongModel,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    onPlay: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(start = 20.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(15.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.albumCoverSmall)
                .crossfade(true)
                .build(),
            contentDescription = null
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = song.artist,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Normal
            )
        }

        IconButton(onClick = onPlay) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Insert song to the entry"
            )
        }
    }
}

@Preview
@Composable
private fun MusicBottomSheetLayoutPreview() {
    MusicBottomSheetLayout(
        songs = ResultWrapper.Success(listOf(Mocks.songModel1, Mocks.songModel2)),
        recentSongs = listOf(Mocks.songModel1, Mocks.songModel2),
        search = "",
        onSearchSong = {},
        onTriggerImmediateSearch = {}
    )
}
