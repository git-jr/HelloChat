package com.paradoxo.hellochat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.paradoxo.hellochat.ui.home.ChatScreen
import com.paradoxo.hellochat.ui.home.ChatScreenUiState
import com.paradoxo.hellochat.ui.home.ChatViewModel
import com.paradoxo.hellochat.ui.theme.HelloChatTheme
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // https://google.github.io/accompanist/navigation-material/
                    BottomSheetNavDemo()
                }
            }
        }
    }
}


private object Destinations {
    const val Home = "HOME"
    const val Sheet = "SHEET"
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun BottomSheetNavDemo() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(navController, Destinations.Home) {
            composable(Destinations.Home) {

                val viewModel = viewModel<ChatViewModel>()
                val state by viewModel.uiState.collectAsState()

                HomeScreen(
                    state = state,
                    sendMessage = {
                        viewModel.sendMessage()
                    },
                    updateshowError = {
                        viewModel.updateshowError()
                    },
                    showSheet = {
                        navController.navigate(Destinations.Sheet)
                    }
                )
            }
            bottomSheet(Destinations.Sheet) {
                BottomSheet(
                    showAnotherSheet = {
                        navController.navigate(Destinations.Sheet)
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    state: ChatScreenUiState,
    sendMessage: () -> Unit,
    updateshowError: () -> Unit,
    showSheet: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    ChatScreen(
        state = state,
        onSendMessage = {
            scope.launch {
                sendMessage()
            }
        },
        onShowSelectorFile = {
            showSheet()
        }
    )

    LaunchedEffect(state.showError) {
        if (state.showError) {
            Toast.makeText(
                context,
                context.getString(R.string.error_message) + state.error,
                Toast.LENGTH_LONG
            ).show()
            updateshowError()
        }
    }
}

@Composable
private fun BottomSheet(showAnotherSheet: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "File type", textAlign = TextAlign.Center)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            IconButton(onClick = showAnotherSheet) {
                Icon(
                    painterResource(id = R.drawable.ic_action_gallery),
                    null,
                    modifier = Modifier
                        .weight(1f),
                )
            }
            IconButton(onClick = showAnotherSheet) {
                Icon(
                    painterResource(id = R.drawable.ic_action_document),
                    null,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            IconButton(onClick = showAnotherSheet) {
                Icon(
                    painterResource(id = R.drawable.ic_action_audio),
                    null,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}