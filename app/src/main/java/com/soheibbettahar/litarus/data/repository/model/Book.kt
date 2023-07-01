package com.soheibbettahar.litarus.data.repository.model

data class Book(
    val id: Long,
    val title: String,
    val authors: List<String>,
    val imageUrl: String,
)