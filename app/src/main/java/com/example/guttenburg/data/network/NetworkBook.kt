package com.example.guttenburg.data.network

data class NetworkBook(
    val id: Long,
    val title: String,
    val subjects: List<String>,
    val authors: List<NetworkPerson>,
    val translators: List<String>,
    val bookshelves: List<String>,
    val languages: List<String>,
    val copyright: Boolean?,
    val media_type: String,
    val formats: NetworkFormats,
    val download_count: Long
)