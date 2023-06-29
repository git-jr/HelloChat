package com.paradoxo.hellochat.ui.home

import com.paradoxo.hellochat.data.Message

data class ChatScreenUiState(
    val messages: List<Message> = emptyList(),
    val messageValue: String = "",
    val onMessageValueChange: (String) -> Unit = {},
    val showError: Boolean = false,
    val error: String = "",
    val languageLastAiMessage: String? = null,
)