package com.soheibbettahar.litarus.util.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

class FirebaseAnalyticsHelper(private val firebaseAnalytics: FirebaseAnalytics) : AnalyticsHelper {

    companion object {
        private const val KEY_MAX_LENGTH = 40
        private const val VALUE_MAX_LENGTH = 100
    }


    override fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.type) {
            for (extra in event.extras) {
                param(
                    key = extra.key.take(KEY_MAX_LENGTH),
                    value = extra.value.take(VALUE_MAX_LENGTH)
                )
            }
        }
    }
}