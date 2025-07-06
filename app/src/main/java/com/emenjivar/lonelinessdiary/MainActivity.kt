package com.emenjivar.lonelinessdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emenjivar.core.data.repositories.DiaryEntryRepository
import com.emenjivar.core.data.repositories.SettingsRepository
import com.emenjivar.core.data.models.DiaryEntry
import com.emenjivar.lonelinessdiary.ui.theme.LonelinessDiaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var diaryEntryRepository: DiaryEntryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val count by settingsRepository.counter.collectAsState(initial = 0)
            val entries by diaryEntryRepository.getAll().collectAsState(initial = emptyList())
            val coroutineScope = rememberCoroutineScope()

            LonelinessDiaryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Text(text = "count: $count")
                        Row {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        settingsRepository.setCounter(count + 1)
                                    }
                                }
                            ) {
                                Text(text = "Increase")
                            }
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        diaryEntryRepository.insert(
                                            DiaryEntry(
                                                title = "Entry ${entries.size + 1}",
                                                content = "Description"
                                            )
                                        )
                                    }
                                }
                            ) {
                                Text(text = "Add entry")
                            }
                        }

                        for (entry in entries) {
                            Text("$entry")
                        }
                    }
                }
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