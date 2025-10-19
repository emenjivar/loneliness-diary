package com.emenjivar.feature.diary.screens.entry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.emenjivar.core.data.models.Mocks
import com.emenjivar.core.data.models.SongModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicBottomSheet(
    sheetState: SheetState,
    songs: List<SongModel>,
    recentSongs: List<SongModel>,
    search: String,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    onSearchSong: (String) -> Unit
) {
    // TODO: add loading state here
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
    songs: List<SongModel>,
    recentSongs: List<SongModel>,
    search: String,
    modifier: Modifier = Modifier,
    onSearchSong: (String) -> Unit
) {
    Card(modifier = modifier) {
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
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close bottomSheet"
                )
            }
        }

        if (recentSongs.isNotEmpty()) {
            Text(text = "Recent songs")
            recentSongs.forEach { song ->
                SongItem(
                    song = song,
                    onClick = {}
                )
            }
        }

        if (songs.isNotEmpty()) {
            Text(text = "Results")
            songs.forEach { song ->
                SongItem(
                    song = song,
                    onClick = {}
                )
            }
        } else {
            Text(text = "No results")
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
        modifier = modifier,
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

        Column {
            Text(text = song.title)
            Text(text = song.albumName)
        }
    }
}

@Preview
@Composable
private fun MusicBottomSheetLayoutPreview() {
    MusicBottomSheetLayout(
        songs = emptyList(),
        recentSongs = listOf(Mocks.songModel1, Mocks.songModel2),
        search = "",
        onSearchSong = {}
    )
}
