package com.decibel.music.ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.decibel.music.domain.mediaservice.handler.ControlState
import com.decibel.music.domain.mediaservice.handler.RepeatState
import com.decibel.music.ui.icon.DecibelIcons
import com.decibel.music.viewModel.UIEvent

@Composable
fun PlayerControlLayout(
    controllerState: ControlState,
    isSmallSize: Boolean = false,
    contentColor: Color = Color.White,
    onUIEvent: (UIEvent) -> Unit,
) {
    val height = if (isSmallSize) 48.dp else 96.dp
    // Keep a 48dp touch target in the compact player while scaling only the glyphs.
    val smallIcon = if (isSmallSize) 20.dp to 48.dp else 32.dp to 42.dp
    val mediumIcon = if (isSmallSize) 28.dp to 48.dp else 42.dp to 52.dp
    val bigIcon = if (isSmallSize) 38.dp to 48.dp else 72.dp to 96.dp
    val activeControlColor = MaterialTheme.colorScheme.primary

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(height)
                .padding(horizontal = 20.dp),
    ) {
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .size(smallIcon.second)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .clickable {
                            onUIEvent(UIEvent.Shuffle)
                        },
                contentAlignment = Alignment.Center,
            ) {
                Crossfade(targetState = controllerState.isShuffle, label = "Shuffle Button") { isShuffle ->
                    Icon(
                        imageVector = DecibelIcons.Shuffle,
                        tint = if (isShuffle) activeControlColor else contentColor,
                        contentDescription = if (isShuffle) "Turn shuffle off" else "Turn shuffle on",
                        modifier = Modifier.size(smallIcon.first),
                    )
                }
            }
        }

        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .size(mediumIcon.second)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .clickable(enabled = controllerState.isPreviousAvailable) {
                            onUIEvent(UIEvent.Previous)
                        },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = DecibelIcons.SkipPrevious,
                    tint = if (controllerState.isPreviousAvailable) contentColor else contentColor.copy(alpha = 0.4f),
                    contentDescription = "Previous track",
                    modifier = Modifier.size(mediumIcon.first),
                )
            }
        }

        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .size(bigIcon.second)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .clickable {
                            onUIEvent(UIEvent.PlayPause)
                        },
                contentAlignment = Alignment.Center,
            ) {
                Crossfade(targetState = controllerState.isPlaying, label = "Play/Pause Button") { isPlaying ->
                    Icon(
                        imageVector = if (isPlaying) DecibelIcons.PauseCircle else DecibelIcons.PlayCircle,
                        tint = contentColor,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(bigIcon.first),
                    )
                }
            }
        }

        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .size(mediumIcon.second)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .clickable(enabled = controllerState.isNextAvailable) {
                            onUIEvent(UIEvent.Next)
                        },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = DecibelIcons.SkipNext,
                    tint = if (controllerState.isNextAvailable) contentColor else contentColor.copy(alpha = 0.4f),
                    contentDescription = "Next track",
                    modifier = Modifier.size(mediumIcon.first),
                )
            }
        }

        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .size(smallIcon.second)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .clickable {
                            onUIEvent(UIEvent.Repeat)
                        },
                contentAlignment = Alignment.Center,
            ) {
                Crossfade(targetState = controllerState.repeatState, label = "Repeat Button") { repeatState ->
                    val repeatColor =
                        if (repeatState is RepeatState.None) contentColor else activeControlColor
                    val repeatDescription =
                        when (repeatState) {
                            is RepeatState.None -> "Turn repeat on"
                            RepeatState.All -> "Repeat all tracks"
                            RepeatState.One -> "Repeat one track"
                        }
                    Icon(
                        imageVector =
                            when (repeatState) {
                                is RepeatState.None, RepeatState.All -> DecibelIcons.Repeat
                                RepeatState.One -> DecibelIcons.RepeatOne
                            },
                        tint = repeatColor,
                        contentDescription = repeatDescription,
                        modifier = Modifier.size(smallIcon.first),
                    )
                }
            }
        }
    }
}
