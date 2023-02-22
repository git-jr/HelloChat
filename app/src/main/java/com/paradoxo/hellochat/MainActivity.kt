package com.paradoxo.hellochat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.paradoxo.hellochat.ui.components.BottomSheetFiles
import com.paradoxo.hellochat.ui.home.ChatScreen
import com.paradoxo.hellochat.ui.home.ChatScreenUiState
import com.paradoxo.hellochat.ui.home.ChatViewModel
import com.paradoxo.hellochat.ui.navigation.HelloChatRoute
import com.paradoxo.hellochat.ui.theme.HelloChatTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloChatTheme {
                HelloChatApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun HelloChatApp() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(navController, HelloChatRoute.HOME) {
            composable(HelloChatRoute.HOME) {

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
                        navController.navigate(HelloChatRoute.BOTTOMSHEETFILE)
                    }
                )
            }
            bottomSheet(HelloChatRoute.BOTTOMSHEETFILE) {
                BottomSheetFiles(
                    showAnotherSheet = {
                        navController.navigate(HelloChatRoute.BOTTOMSHEETFILE)
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

