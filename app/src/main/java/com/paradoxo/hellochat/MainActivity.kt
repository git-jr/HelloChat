package com.paradoxo.hellochat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.paradoxo.hellochat.data.Author
import com.paradoxo.hellochat.data.Message
import com.paradoxo.hellochat.ui.components.BottomSheetFiles
import com.paradoxo.hellochat.ui.home.ChatScreen
import com.paradoxo.hellochat.ui.home.ChatScreenUiState
import com.paradoxo.hellochat.ui.home.ChatViewModel
import com.paradoxo.hellochat.ui.navigation.HelloChatRoute
import com.paradoxo.hellochat.ui.theme.HelloChatTheme
import kotlinx.coroutines.delay
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

    val context = LocalContext.current

    val viewModel = viewModel<ChatViewModel>()
    val state by viewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {

            scope.launch {
                viewModel.indentificationImage(it)
            }

            it?.let { uri ->
                Log.i("uri", uri.toString())

                navController.navigateUp()

                val image: InputImage
                try {
                    image = InputImage.fromFilePath(context, uri)
                    classifyImage(image) { labels ->

                        if (labels.isNotEmpty()) {
                            val message = Message(
                                content = labels.toString(), autor = Author.AI, visualContent = "teste"
                            )
                            scope.launch {
                                delay(1000)
                                viewModel.addResponse(message)
                            }
                        }

                    }
                } catch (e: IOException) {
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
                    },
                    onSelectImage = {
                        pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                )
            }
        }
    }
}


fun classifyImage(image: InputImage, onResult: (List<String>) -> Unit) {

    val listlabels = mutableListOf<String>()

    val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
    labeler.process(image)
        .addOnSuccessListener { labels ->
            for (label in labels) {
                val text = label.text
                val confidence = label.confidence
                val index = label.index
                Log.i("label", "$text $confidence $index")

                listlabels.add(text)
            }
            onResult(listlabels)
        }
        .addOnFailureListener { e ->
            Log.i("label", e.toString())
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

