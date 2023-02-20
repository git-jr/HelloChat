package com.paradoxo.hellochat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.paradoxo.hellochat.ui.home.ChatScreen
import com.paradoxo.hellochat.ui.home.ChatViewModel
import com.paradoxo.hellochat.ui.theme.HelloChatTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel by viewModels<ChatViewModel>()
                    val state by viewModel.uiState.collectAsState()

                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()

                    ChatScreen(
                        state = state,
                        onSendMessage = {
                            scope.launch {
                                viewModel.sendMessage()
                            }
                        },
                    )

                    LaunchedEffect(state.showError) {
                        if (state.showError) {
                            Toast.makeText(
                                context,
                                getString(R.string.error_message) + state.error,
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.updateshowError()
                        }
                    }
                }
            }
        }
    }
}