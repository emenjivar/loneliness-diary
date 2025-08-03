package com.emenjivar.feature.diary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.SharedFlow

/**
 * Utility composable to handle navigation events.
 */
@Composable
fun HandleNavigation(
    navigationFlow: SharedFlow<NavigationAction?>,
    onNavigateAction: (NavigationAction) -> Unit
) {
    val navigationAction by navigationFlow.collectAsStateWithLifecycle(
        initialValue = null
    )
    LaunchedEffect(navigationAction) {
        navigationAction?.let(onNavigateAction)
    }
}
