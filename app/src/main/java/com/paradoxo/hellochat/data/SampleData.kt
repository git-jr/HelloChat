package com.paradoxo.hellochat.data

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

val messageListSample = listOf(
    Message("Olá", Author.USER),
    Message(LoremIpsum(2).values.first(), Author.AI),
    Message(
        LoremIpsum(13).values.first(),
        Author.USER
    ),
    Message(LoremIpsum(14).values.last(), Author.AI),
)
