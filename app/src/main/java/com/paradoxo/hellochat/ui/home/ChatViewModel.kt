package com.paradoxo.hellochat.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.TranslateLanguage
import com.paradoxo.hellochat.data.Author
import com.paradoxo.hellochat.data.Message
import com.paradoxo.hellochat.data.messageListSample
import com.paradoxo.hellochat.mlkit.TextTranslate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val textTranslate: TextTranslate = TextTranslate()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatScreenUiState())
    val uiState: StateFlow<ChatScreenUiState>
        get() = _uiState.asStateFlow()


    init {
        _uiState.update { state ->
            state.copy(onMessageValueChange = {
                _uiState.value = _uiState.value.copy(
                    messageValue = it
                )
            })
        }

        _uiState.value = _uiState.value.copy(
            messages = messageListSample,
        )
    }

    fun sendMessage() {
        with(_uiState) {
            val messageValue = value.messageValue
            updateUi()
            searchResponse(messageValue)
        }
    }

    private fun searchResponse(messageValue: String) {

        val responseMessage = Message(
            content = "Feature avaliable soon, for now, try to send an image!",
            autor = Author.AI
        )

        viewModelScope.launch {
            textTranslate.indentifyLanguage(
                messageValue,
                onSuccessful = { userLanguage ->
                    val languageLastAiMessage =
                        _uiState.value.languageLastAiMessage ?: TranslateLanguage.ENGLISH
                    if (userLanguage != languageLastAiMessage) {
                        val contenLastAiMessage =
                            _uiState.value.messages.last { it.autor == Author.AI }.content

                        textTranslate.translateText(
                            text = contenLastAiMessage,
                            targetLanguage = userLanguage,
                            sourceLanguage = languageLastAiMessage,
                            onSuccessful = { translatedText ->
                                val translateMessage = Message(
                                    content = translatedText,
                                    autor = Author.AI
                                )
                                addResponse(translateMessage)
                            },
                            onFailiure = {
                                addNewMessageAndRemoveLoad(responseMessage)
                            }
                        )
                    } else {
                        addNewMessageAndRemoveLoad(responseMessage)
                    }

                },
                onFailiure = {
                    addNewMessageAndRemoveLoad(responseMessage)
                }
            )
        }
    }

    private fun addNewMessageAndRemoveLoad(responseMessage: Message) {
        with(_uiState) {
            val messages = value.messages.toMutableList()
            if (messages[messages.size - 1].autor == Author.LOAD) {
                messages.removeAt(messages.size - 1)
            }
            messages.add(responseMessage)
            value = value.copy(
                messages = messages
            )
        }
    }

    private fun updateUi() {
        with(_uiState) {
            val userMessage = Message(
                content = value.messageValue, autor = Author.USER,
            )

            value = value.copy(
                messages = value.messages.plus(
                    listOf(
                        userMessage,
                        Message(autor = Author.LOAD)
                    )
                ),
                messageValue = ""
            )
        }
    }

    fun updateShowError() {
        _uiState.value = _uiState.value.copy(
            showError = false,
            error = "Don't was possible to classify the image"
        )
    }

    private fun addResponse(message: Message) {
        with(_uiState) {
            val messages = value.messages.toMutableList()
            messages.removeAt(messages.size - 1)
            messages.add(message)
            value = value.copy(
                messages = messages
            )
        }

        if (message.autor == Author.AI) {
            textTranslate.indentifyLanguage(
                message.content,
                onSuccessful = { languageLastAiMessage ->
                    _uiState.value = _uiState.value.copy(
                        languageLastAiMessage = languageLastAiMessage
                    )
                }
            )
        }
    }

    fun indentificationImage(uri: Uri?) {
        val userMessage = Message(
            content = "Image $uri",
            autor = Author.USER,
            visualContent = uri.toString()
        )

        val loadMessage = Message(autor = Author.LOAD)

        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages.plus(
                listOf(
                    userMessage,
                    loadMessage
                )
            )
        )
    }

    fun addAiMessage(value: MutableList<String>) {

        val text = value.toString()

        _uiState.value.languageLastAiMessage?.let { languageLastAiMessage ->
            textTranslate.translateText(
                text = text,
                targetLanguage = languageLastAiMessage,
                sourceLanguage = TranslateLanguage.ENGLISH,
                onSuccessful = { translatedText ->
                    val translateMessage = Message(
                        content = translatedText,
                        autor = Author.AI
                    )
                    addResponse(translateMessage)
                },
                onFailiure = {
                    addResponse(Message(autor = Author.AI))
                }
            )
        } ?: run {
            viewModelScope.launch {
                delay(1000)
                addResponse(Message(content = text, autor = Author.AI))
            }
        }
    }
}