package com.emenjivar.feature.diary.screens.entry.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.emenjivar.core.data.models.Mocks
import com.emenjivar.core.data.models.SongModel
import com.emenjivar.core.data.utils.ResultWrapper
import com.emenjivar.feature.diary.ui.LocalCoilImageLoaderProvider
import com.emenjivar.feature.diary.ui.LocalExoplayerProvider
import com.emenjivar.feature.diary.util.DELAY_FOCUS
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @param sheetState The state of the bottom sheet.
 * @param songs The list of songs fetched from API.
 * @param recentSongs The list of previously selected songs.
 * @param search The search query string.
 * @param modifier Modifier to be applied to the bottom sheet component.
 * @param onSearchSong Callback triggered when the search input changes.
 * @param onTriggerImmediateSearch Callback trigger when the ime action `search` is called.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Stable
fun MusicBottomSheet(
    sheetState: BottomSheetStateWithData<Unit>,
    songs: ResultWrapper<List<SongModel>>,
    recentSongs: List<SongModel>,
    search: String,
    modifier: Modifier = Modifier,
    onSearchSong: (String) -> Unit,
    onTriggerImmediateSearch: () -> Unit,
    onClickSong: (SongModel) -> Unit,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val showBottomSheet by sheetState.showBottomSheet.collectAsStateWithLifecycle()
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier.statusBarsPadding(),
            sheetState = sheetState.sheetState,
            dragHandle = null,
            sheetGesturesEnabled = false,
            onDismissRequest = {
                coroutineScope
                    .launch { sheetState.hide() }
                    .invokeOnCompletion { onDismiss() }
            }
        ) {
            MusicBottomSheetLayout(
                songs = songs,
                recentSongs = recentSongs,
                search = search,
                modifier = Modifier.fillMaxSize(),
                onSearchSong = onSearchSong,
                onTriggerImmediateSearch = onTriggerImmediateSearch,
                onClickSong = onClickSong,
                onDismiss = {
                    coroutineScope
                        .launch { sheetState.hide() }
                        .invokeOnCompletion { onDismiss() }
                }
            )
        }
    }
}

@ExperimentalMaterial3Api
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
@Stable
private fun MusicBottomSheetLayout(
    songs: ResultWrapper<List<SongModel>>,
    recentSongs: List<SongModel>,
    search: String,
    modifier: Modifier = Modifier,
    onSearchSong: (String) -> Unit,
    onTriggerImmediateSearch: () -> Unit,
    onClickSong: (SongModel) -> Unit,
    onDismiss: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentMediaItemIndex by remember { mutableIntStateOf(-1) }
    val exoplayer = LocalExoplayerProvider.current.exoPlayer
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val localKeyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        exoplayer.apply {
            pauseAtEndOfMediaItems = true
            addListener(
                object : Player.Listener {
                    override fun onIsPlayingChanged(playing: Boolean) {
                        isPlaying = playing
                    }

                    override fun onMediaItemTransition(
                        mediaItem: MediaItem?,
                        reason: Int
                    ) {
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

    LaunchedEffect(Unit) {
        delay(DELAY_FOCUS)
        focusRequester.requestFocus()
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            focusRequester.freeFocus()
            localKeyboard?.hide()
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
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            navigationIcon = {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Close music search bottom sheet"
                    )
                }
            },
            title = {}
        )

        BasicTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .padding(horizontal = 20.dp),
            value = search,
            textStyle = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onTriggerImmediateSearch()
                }
            ),
            onValueChange = onSearchSong,
            decorationBox = { innerBox ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                    ) {
                        innerBox()
                        if (search.isBlank()) {
                            Text(
                                modifier = Modifier,
                                text = "Search a song",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    IconButton(onClick = { onSearchSong("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close bottomSheet"
                        )
                    }
                }
            }
        )

        LazyColumn(
            modifier = Modifier.padding(vertical = 20.dp),
            state = listState
        ) {
            if (recentSongs.isNotEmpty()) {
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = "Recent songs",
                        style = MaterialTheme.typography.labelMedium
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
                            onClickSong(song)
                        }
                    )
                }
            }

            when (songs) {
                is ResultWrapper.Loading -> {
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = "Searching...",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                is ResultWrapper.Success -> {
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = "Results:",
                            style = MaterialTheme.typography.labelLarge
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
                                onClickSong(song)
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
            .clickable { onClick() }
            .padding(start = 20.dp)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(15.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.albumCoverSmall)
                .crossfade(true)
                // In theory, different song for the same album shares the cover image
                // I think this is a good optimization for save some memory/disk space
                .diskCacheKey(song.albumId.toString())
                .memoryCacheKey(song.albumId.toString())
                .build(),
            imageLoader = LocalCoilImageLoaderProvider.current.loader,
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MusicBottomSheetLayoutPreview() {
    MusicBottomSheetLayout(
        songs = ResultWrapper.Success(listOf(Mocks.songModel1, Mocks.songModel2)),
        recentSongs = listOf(Mocks.songModel1, Mocks.songModel2),
        search = "",
        onSearchSong = {},
        onTriggerImmediateSearch = {},
        onClickSong = {},
        onDismiss = {}
    )
}
