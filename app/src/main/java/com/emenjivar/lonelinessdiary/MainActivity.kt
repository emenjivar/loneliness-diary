package com.emenjivar.lonelinessdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.emenjivar.feature.diary.navigation.DiaryRoute
import com.emenjivar.feature.diary.navigation.featureGraph
import com.emenjivar.lonelinessdiary.ui.theme.LonelinessDiaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val backStack = rememberNavBackStack(DiaryRoute)

            LonelinessDiaryTheme {
                NavDisplay(
                    backStack = backStack,
                    entryProvider = entryProvider {
                        featureGraph(
                            navigateTo = backStack::add,
                            popBackStack = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LonelinessDiaryTheme {
        Greeting("Android")
    }
}
