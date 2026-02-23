package com.emenjivar.feature.diary.screens.entry.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.emenjivar.core.data.models.Mocks
import com.emenjivar.core.data.models.SongModel
import com.emenjivar.feature.diary.ui.ExoplayerProvider
import com.emenjivar.feature.diary.ui.LocalExoplayerProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Stable
fun MusicDetailBottomSheet(
    sheetState: BottomSheetStateWithData<SongModel>,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val showBottomSheet by sheetState.showBottomSheet.collectAsStateWithLifecycle()

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState.sheetState,
            onDismissRequest = {
                coroutineScope
                    .launch { sheetState.hide() }
                    .invokeOnCompletion { onDismiss() }
            }
        ) {
            MusicDetailBottomSheetLayout(
                song = sheetState.data
            )
        }
    }
}

private val ImageSize = 100.dp
private val InnerPadding = 20.dp

@Composable
@Stable
private fun MusicDetailBottomSheetLayout(
    song: SongModel,
    modifier: Modifier = Modifier,
) {
    var isPlaying by remember { mutableStateOf(false) }
    val exoplayer = LocalExoplayerProvider.current.exoPlayer

    LaunchedEffect(Unit) {
        exoplayer.apply {
            addListener(
                object : Player.Listener {
                    override fun onIsPlayingChanged(playing: Boolean) {
                        isPlaying = playing
                    }
                }
            )
        }
    }

    LaunchedEffect(song) {
        exoplayer.apply {
            val song = MediaItem.fromUri(song.previewUrl)
            clearMediaItems()
            setMediaItem(song)
            repeatMode = Player.REPEAT_MODE_OFF
            prepare()
            play()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        exoplayer.pause()
    }

    DisposableEffect(Unit) {
        // Do not release the global exoplayer
        onDispose { exoplayer.clearMediaItems() }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(InnerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(ImageSize)
                    .clip(RoundedCornerShape(15.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.albumCover)
                    .crossfade(true)
                    .diskCacheKey(song.albumCover)
                    .memoryCacheKey(song.albumCover)
                    .build(),
                contentDescription = "Cover of the song"
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = song.title,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = song.artist,
                style = MaterialTheme.typography.titleSmall
            )

            IconButton(
                onClick = {
                    if (isPlaying) {
                        exoplayer.pause()
                    } else {
                        exoplayer.play()
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play song"
                )
            }
        }
    }
}

@Preview
@Composable
private fun MusicDetailBottomSheetPreview() {
    CompositionLocalProvider(
        LocalExoplayerProvider provides ExoplayerProvider(
            exoPlayer = ExoPlayer.Builder(LocalContext.current).build()
        )
    ) {
        MusicDetailBottomSheetLayout(
            song = Mocks.songModel1
        )
    }
}