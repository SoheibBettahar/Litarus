package com.soheibbettahar.litarus.util.analytics

data class Param(val key: String, val value: String)

data class AnalyticsEvent(val type: String, val extras: List<Param> = listOf())