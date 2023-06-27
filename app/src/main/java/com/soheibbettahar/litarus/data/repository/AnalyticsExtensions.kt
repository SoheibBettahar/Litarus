package com.soheibbettahar.litarus.data.repository

import com.soheibbettahar.litarus.data.repository.model.BookWithExtras
import com.soheibbettahar.litarus.util.analytics.AnalyticsEvent
import com.soheibbettahar.litarus.util.analytics.AnalyticsHelper
import com.soheibbettahar.litarus.util.analytics.Param


fun AnalyticsHelper.logDownloadBook(book: BookWithExtras) {
    val event = AnalyticsEvent(
        type = "download",
        extras = listOf(
            Param("id", book.id.toString()),
            Param("title", book.title),
            Param("authors", book.authors),
        )
    )

    logEvent(event = event)
}

fun AnalyticsHelper.logCancelDownloadBook(book: BookWithExtras) {
    val event = AnalyticsEvent(
        type = "cancel_download",
        extras = listOf(
            Param("id", book.id.toString()),
            Param("title", book.title),
            Param("authors", book.authors),
        )
    )

    logEvent(event = event)
}


fun AnalyticsHelper.logSearchBook(term: String, category: String, languages: String) {
    val event = AnalyticsEvent(
        type = "search",
        extras = listOf(
            Param("search_term", term),
            Param("category", category),
            Param("languages", languages),
        )
    )

    logEvent(event = event)
}