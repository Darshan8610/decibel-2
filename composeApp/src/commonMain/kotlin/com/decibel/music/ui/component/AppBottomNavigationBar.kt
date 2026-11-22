package com.decibel.music.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.decibel.music.extension.artworkScrimBrush
import com.decibel.music.ui.navigation.destination.home.HomeDestination
import com.decibel.music.ui.navigation.destination.library.LibraryDestination
import com.decibel.music.ui.navigation.destination.search.SearchDestination
import com.decibel.music.ui.theme.typo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.decibel.music.composeapp.generated.resources.*
import kotlin.reflect.KClass

@Composable
fun AppBottomNavigationBar(
    startDestination: Any = HomeDestination,
    navController: NavController,
    isTranslucentBackground: Boolean = false,
    reloadDestinationIfNeeded: (KClass<*>) -> Unit = { _ -> },
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomNavScreens =
        listOf(
            BottomNavScreen.Home,
            BottomNavScreen.Search,
            BottomNavScreen.Library,
        )
    val routeSelectedIndex =
        bottomNavScreens.firstOrNull { screen ->
            currentBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute(screen.destination::class)
            } == true
        }?.ordinal
    var lastSelectedIndex by rememberSaveable {
        mutableIntStateOf(
            when (startDestination) {
                is HomeDestination -> BottomNavScreen.Home.ordinal
                is SearchDestination -> BottomNavScreen.Search.ordinal
                is LibraryDestination -> BottomNavScreen.Library.ordinal
                else -> BottomNavScreen.Home.ordinal
            },
        )
    }
    LaunchedEffect(routeSelectedIndex) {
        if (routeSelectedIndex != null) lastSelectedIndex = routeSelectedIndex
    }
    val selectedIndex = routeSelectedIndex ?: lastSelectedIndex
    val activeTint = MaterialTheme.colorScheme.primary
    val inactiveTint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f)

    Box(
        modifier =
            Modifier
                .wrapContentSize()
                .then(
                    if (isTranslucentBackground) {
                        Modifier.background(artworkScrimBrush(MaterialTheme.colorScheme.surface))
                    } else {
                        Modifier
                    },
                ),
    ) {
        NavigationBar(
            windowInsets = WindowInsets(0, 0, 0, 0),
            containerColor =
                if (isTranslucentBackground) {
                    Color.Transparent
                } else {
                    MaterialTheme.colorScheme.surface
                },
        ) {
            bottomNavScreens.forEach { screen ->
                val routeIsCurrent =
                    currentBackStackEntry?.destination?.hierarchy?.any {
                        it.hasRoute(screen.destination::class)
                    } == true
                val isSelected = selectedIndex == screen.ordinal

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (routeIsCurrent) {
                            reloadDestinationIfNeeded(screen.destination::class)
                        } else {
                            lastSelectedIndex = screen.ordinal
                            navController.navigate(screen.destination) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    label = {
                        Text(
                            stringResource(screen.title),
                            style =
                                typo().bodySmall.copy(
                                    color = if (isSelected) activeTint else inactiveTint,
                                ),
                        )
                    },
                    icon = screen.icon,
                    colors =
                        NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            selectedIconColor = activeTint,
                            unselectedIconColor = inactiveTint,
                            selectedTextColor = activeTint,
                            unselectedTextColor = inactiveTint,
                        ),
                    modifier =
                        Modifier.windowInsetsPadding(
                            NavigationBarDefaults.windowInsets,
                        ),
                )
            }
        }
    }
}

@Composable
fun AppNavigationRail(
    startDestination: Any = HomeDestination,
    navController: NavController,
    reloadDestinationIfNeeded: (KClass<*>) -> Unit = { _ -> },
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomNavScreens =
        listOf(
            BottomNavScreen.Home,
            BottomNavScreen.Search,
            BottomNavScreen.Library,
        )
    val routeSelectedIndex =
        bottomNavScreens.firstOrNull { screen ->
            currentBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute(screen.destination::class)
            } == true
        }?.ordinal
    var lastSelectedIndex by rememberSaveable {
        mutableIntStateOf(
            when (startDestination) {
                is HomeDestination -> BottomNavScreen.Home.ordinal
                is SearchDestination -> BottomNavScreen.Search.ordinal
                is LibraryDestination -> BottomNavScreen.Library.ordinal
                else -> BottomNavScreen.Home.ordinal
            },
        )
    }
    LaunchedEffect(routeSelectedIndex) {
        if (routeSelectedIndex != null) lastSelectedIndex = routeSelectedIndex
    }
    val selectedIndex = routeSelectedIndex ?: lastSelectedIndex
    val activeTint = MaterialTheme.colorScheme.primary
    val inactiveTint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f)
    NavigationRail {
        Spacer(Modifier.height(16.dp))
        Box(Modifier.padding(horizontal = 16.dp)) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(Res.drawable.mono),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .height(32.dp)
                            .clip(CircleShape),
                )
            }
        }
        Spacer(Modifier.weight(1f))
        bottomNavScreens.forEach { screen ->
            val routeIsCurrent =
                currentBackStackEntry?.destination?.hierarchy?.any {
                    it.hasRoute(screen.destination::class)
                } == true
            val isSelected = selectedIndex == screen.ordinal

            NavigationRailItem(
                icon = screen.icon,
                label = {
                    Text(
                        stringResource(screen.title),
                        style =
                            typo().bodySmall.copy(
                                color = if (isSelected) activeTint else inactiveTint,
                            ),
                    )
                },
                selected = isSelected,
                onClick = {
                    if (routeIsCurrent) {
                        reloadDestinationIfNeeded(screen.destination::class)
                    } else {
                        lastSelectedIndex = screen.ordinal
                        navController.navigate(screen.destination) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            )
        }
        Spacer(Modifier.height(32.dp))
    }
}
