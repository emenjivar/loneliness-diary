package com.emenjivar.feature.diary.screens.entry.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    onSearchSong: (String) -> Unit
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
                onSearchSong = onSearchSong
            )
        }
    }
}

@Composable
@Stable
private fun MusicBottomSheetLayout(
    songs: ResultWrapper<List<SongModel>>,
    recentSongs: List<SongModel>,
    search: String,
    modifier: Modifier = Modifier,
    onSearchSong: (String) -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = search,
                placeholder = {
                    Text(text = "Search a song")
                },
                onValueChange = onSearchSong
            )

            IconButton(
                onClick = {
                    onSearchSong("")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close bottomSheet"
                )
            }
        }

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

                items(recentSongs) { song ->
                    SongItem(
                        song = song,
                        onClick = {}
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
                            text = "Results"
                        )
                    }
                    items(songs.data) { song ->
                        SongItem(
                            song = song,
                            onClick = {}
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.padding(start = 20.dp),
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
                text = song.albumName,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Normal
            )
        }

        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.Add,
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
        onSearchSong = {}
    )
}
