package com.paradoxo.hellochat.data

data class Message(
    val content: String = "",
    val autor: Author = Author.AI,
    val visualContent: String? = null
)

enum class Author {
    LOAD, USER, AI
}