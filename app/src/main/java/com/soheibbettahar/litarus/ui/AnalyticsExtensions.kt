package com.soheibbettahar.litarus.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.soheibbettahar.litarus.util.analytics.AnalyticsEvent
import com.soheibbettahar.litarus.util.analytics.AnalyticsHelper
import com.soheibbettahar.litarus.util.analytics.LocalAnalyticsHelper
import com.soheibbettahar.litarus.util.analytics.Param


/**
 * Classes and functions associated with analytics events for the UI.
 */
fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        AnalyticsEvent(
            type = "screen_view",
            extras = listOf(
                Param("screen_name", screenName),
            ),
        ),
    )
}

fun AnalyticsHelper.openBook(id: String, title: String, author: String) {
    logEvent(
        event = AnalyticsEvent(
            type = "open",
            extras = listOf(
                Param("id", id),
                Param("title", title),
                Param("author", author),
            ),
        ),
    )
}


fun AnalyticsHelper.shareBook(id: String, title: String, author: String) {
    logEvent(
        event = AnalyticsEvent(
            type = "share",
            extras = listOf(
                Param("id", id),
                Param("title", title),
                Param("author", author),
            ),
        ),
    )
}

/**
 * A side-effect which records a screen view event.
 */
@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose {}
}