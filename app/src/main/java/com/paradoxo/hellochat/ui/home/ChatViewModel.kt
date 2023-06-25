package com.paradoxo.hellochat.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.paradoxo.hellochat.data.Author
import com.paradoxo.hellochat.data.Message
import com.paradoxo.hellochat.data.messageListSample
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel : ViewModel() {
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
    }

    private fun updateUi() {
        with(_uiState) {
            val userMessage = Message(
                content = value.messageValue, autor = Author.USER
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

    fun updateshowError() {
        _uiState.value = _uiState.value.copy(
            showError = false,
            error = ""
        )
    }

    fun addResponse(message: Message) {
        with(_uiState) {
            val messages = value.messages.toMutableList()
            messages.removeAt(messages.size - 1)
            messages.add(message)
            value = value.copy(
                messages = messages
            )
        }
    }

    fun indentificationImage(uri: Uri?) {
        // adicionar mensagem de "enviado" e depois carregamento
        val userMessage = Message(
            content = "Imagem $uri", autor = Author.USER
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
}