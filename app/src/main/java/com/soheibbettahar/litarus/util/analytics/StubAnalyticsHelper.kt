package com.soheibbettahar.litarus.util.analytics

import android.util.Log
import javax.inject.Inject

private const val TAG = "StubAnalyticsHelper"

/**
 * Implementation of AnalyticsHelper which is used in Debug mode.
 */
class StubAnalyticsHelper @Inject constructor() : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        Log.d(TAG, "Received analytics event: $event")
    }
}