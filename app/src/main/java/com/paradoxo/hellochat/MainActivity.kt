package com.paradoxo.hellochat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.paradoxo.hellochat.mlkit.ImageLabeling
import com.paradoxo.hellochat.ui.components.BottomSheetFiles
import com.paradoxo.hellochat.ui.home.ChatScreen
import com.paradoxo.hellochat.ui.home.ChatScreenUiState
import com.paradoxo.hellochat.ui.home.ChatViewModel
import com.paradoxo.hellochat.ui.navigation.HelloChatRoute
import com.paradoxo.hellochat.ui.theme.HelloChatTheme
import kotlinx.coroutines.launch
import java.io.IOException

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

    val chatViewModel = viewModel<ChatViewModel>()
    val state by chatViewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val imageLabeling = ImageLabeling(context)

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            scope.launch {
                chatViewModel.indentificationImage(it)
            }

            it?.let { uri ->
                navController.navigateUp()
                try {
                    imageLabeling.classifyImage(
                        uri,
                        onSuccessful = { labels ->
                            chatViewModel.addAiMessage(labels)
                        },
                        onFailiure = {
                            chatViewModel.updateshowError()
                        }
                    )

                } catch (e: IOException) {
                    chatViewModel.updateshowError()
                    e.printStackTrace()
                }
            }
        }
    )

    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(navController, HelloChatRoute.HOME) {
            composable(HelloChatRoute.HOME) {

                HomeScreen(
                    state = state,
                    sendMessage = {
                        chatViewModel.sendMessage()
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
                    },
                    onSelectImage = {
                        pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
    showSheet: () -> Unit,
) {
    ChatScreen(
        state = state,
        onSendMessage = {
            sendMessage()
        },
        onShowSelectorFile = {
            showSheet()
        }
    )
}

