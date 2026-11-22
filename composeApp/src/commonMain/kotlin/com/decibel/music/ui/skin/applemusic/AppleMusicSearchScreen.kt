package com.decibel.music.ui.skin.applemusic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.decibel.music.common.Config
import com.decibel.music.domain.mediaservice.handler.PlaylistType
import com.decibel.music.domain.mediaservice.handler.QueueData
import com.decibel.music.domain.utils.toTrack
import com.decibel.music.ui.component.SongFullWidthItems
import com.decibel.music.ui.icon.DecibelIcons
import com.decibel.music.ui.navigation.destination.home.MoodDestination
import com.decibel.music.ui.skin.AppleMusicPink
import com.decibel.music.ui.skin.AppleMusicSurfaceDark
import com.decibel.music.ui.skin.AppleMusicSurfaceElevatedDark
import com.decibel.music.ui.skin.AppleMusicTextSecondaryDark
import com.decibel.music.ui.theme.typo
import com.decibel.music.viewModel.SearchScreenUIState
import com.decibel.music.viewModel.SearchViewModel
import com.decibel.music.viewModel.SharedViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppleMusicSearchScreen(
    searchViewModel: SearchViewModel = koinViewModel(),
    navController: NavController,
) {
    val searchState by searchViewModel.searchScreenState.collectAsStateWithLifecycle()
    val searchUiState by searchViewModel.searchScreenUIState.collectAsStateWithLifecycle()
    val moodAndGenres by searchViewModel.moodAndGenres.collectAsStateWithLifecycle()
    val moodArtwork by searchViewModel.moodArtwork.collectAsStateWithLifecycle()
    val sharedViewModel: SharedViewModel = koinInject()

    var searchText by rememberSaveable { mutableStateOf("") }
    var submittedQuery by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    fun submitSearch(query: String) {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty()) return

        submittedQuery = cleanQuery
        searchText = cleanQuery
        searchViewModel.insertSearchHistory(cleanQuery)
        searchViewModel.searchSongs(cleanQuery)
        focusManager.clearFocus()
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 132.dp),
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 12.dp),
            ) {
                Text(
                    text = "Search",
                    style = typo().headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 34.sp,
                    ),
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                Card(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AppleMusicSurfaceDark),
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = DecibelIcons.Search,
                            contentDescription = "Search",
                            tint = AppleMusicTextSecondaryDark,
                            modifier = Modifier.size(19.dp),
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        BasicTextField(
                            value = searchText,
                            onValueChange = { value ->
                                searchText = value
                                if (submittedQuery.isNotBlank() && value.trim() != submittedQuery) {
                                    submittedQuery = ""
                                }
                            },
                            modifier = Modifier.weight(1f).focusRequester(focusRequester),
                            singleLine = true,
                            textStyle = typo().bodyMedium.copy(color = Color.White, fontSize = 16.sp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { submitSearch(searchText) }),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (searchText.isEmpty()) {
                                        Text(
                                            "Artists, songs, albums and more",
                                            style = typo().bodyMedium.copy(
                                                color = AppleMusicTextSecondaryDark,
                                                fontSize = 15.sp,
                                            ),
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                        )
                        if (searchText.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchText = ""
                                    submittedQuery = ""
                                    focusManager.clearFocus()
                                },
                            ) {
                                Text("×", color = AppleMusicTextSecondaryDark, fontSize = 22.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = if (submittedQuery.isBlank()) "Browse Categories" else "Results",
                    style = typo().headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 21.sp,
                    ),
                )
                if (submittedQuery.isNotBlank()) {
                    Text(
                        text = "For \"" + submittedQuery + "\"",
                        style = typo().bodyMedium.copy(color = AppleMusicTextSecondaryDark),
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }

        if (submittedQuery.isNotBlank()) {
            when (searchUiState) {
                SearchScreenUIState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircularProgressIndicator(
                                color = AppleMusicPink,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp),
                            )
                            Spacer(modifier = Modifier.size(12.dp))
                            Text("Searching", color = AppleMusicTextSecondaryDark)
                        }
                    }
                }

                SearchScreenUIState.Error -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                "Couldn't load these results",
                                color = Color.White,
                                style = typo().titleMedium,
                            )
                            Text(
                                "Check your connection and try again.",
                                color = AppleMusicTextSecondaryDark,
                                modifier = Modifier.padding(top = 6.dp),
                            )
                            TextButton(onClick = { searchViewModel.searchSongs(submittedQuery) }) {
                                Text("Try again", color = AppleMusicPink)
                            }
                        }
                    }
                }

                SearchScreenUIState.Success -> {
                    if (searchState.searchSongsResult.isEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                "No songs found for \"" + submittedQuery + "\".",
                                color = AppleMusicTextSecondaryDark,
                                modifier = Modifier.padding(vertical = 28.dp),
                            )
                        }
                    } else {
                        items(
                            items = searchState.searchSongsResult,
                            key = { it.videoId },
                            span = { GridItemSpan(maxLineSpan) },
                        ) { song ->
                            val track = song.toTrack()
                            SongFullWidthItems(
                                track = track,
                                isPlaying = false,
                                onClickListener = {
                                    searchViewModel.setQueueData(
                                        QueueData.Data(
                                            listTracks = arrayListOf(track),
                                            firstPlayedTrack = track,
                                            playlistId = "RDAMVM" + track.videoId,
                                            playlistName = submittedQuery,
                                            playlistType = PlaylistType.RADIO,
                                            continuation = null,
                                        ),
                                    )
                                    searchViewModel.loadMediaItem(track, type = Config.SONG_CLICK)
                                },
                                onAddToQueue = { sharedViewModel.addListToQueue(arrayListOf(track)) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }

                SearchScreenUIState.Empty -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            "Search for a song to see results here.",
                            color = AppleMusicTextSecondaryDark,
                            modifier = Modifier.padding(vertical = 28.dp),
                        )
                    }
                }
            }
        } else {
            val mood = moodAndGenres
            if (mood == null) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 36.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CircularProgressIndicator(
                            color = AppleMusicPink,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp),
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Text("Loading categories", color = AppleMusicTextSecondaryDark)
                    }
                }
            } else {
                mood.sections.forEach { section ->
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = section.title,
                            style = typo().titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp,
                            ),
                            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
                        )
                    }
                    items(
                        items = section.items,
                        key = { section.title + "/" + it.params },
                    ) { category ->
                        LaunchedEffect(category.params) {
                            searchViewModel.loadMoodArtwork(category.params)
                        }
                        AppleMusicCategoryCard(
                            title = category.title,
                            artworkUrl = moodArtwork[category.params],
                            onClick = {
                                navController.navigate(MoodDestination(params = category.params))
                            },
                        )
                    }
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun AppleMusicCategoryCard(
    title: String,
    artworkUrl: String?,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.12f)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        AppleMusicPink.copy(alpha = 0.24f),
                        AppleMusicSurfaceElevatedDark,
                        AppleMusicSurfaceDark,
                    ),
                ),
            )
            .clickable(onClick = onClick),
    ) {
        if (!artworkUrl.isNullOrBlank()) {
            AsyncImage(
                model = artworkUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0x08000000),
                            Color(0x22000000),
                            Color(0xE6000000),
                        ),
                    ),
                ),
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(AppleMusicPink.copy(alpha = 0.9f)),
        )
        Text(
            text = title,
            style = typo().titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 17.sp,
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp),
        )
    }
}
