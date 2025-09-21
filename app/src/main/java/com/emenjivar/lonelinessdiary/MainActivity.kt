package com.emenjivar.lonelinessdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.emenjivar.feature.diary.navigation.NavigationAction
import com.emenjivar.feature.diary.navigation.featureGraph
import com.emenjivar.feature.diary.screens.browse.DiaryBrowseRoute
import com.emenjivar.lonelinessdiary.ui.theme.LonelinessDiaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val backStack = rememberNavBackStack(DiaryBrowseRoute)

            LonelinessDiaryTheme {
                NavDisplay(
                    backStack = backStack,
                    entryProvider = entryProvider {
                        featureGraph(
                            onNavigateAction = { action ->
                                when (action) {
                                    is NavigationAction.NavigateTo -> {
                                        backStack.add(action.route)
                                    }

                                    NavigationAction.PopBackStack -> {
                                        backStack.removeLastOrNull()
                                    }
                                }
                            }
                        )
                    }
                )
            }
        }
    }
}
