package com.soheibbettahar.litarus.data.network.googleBooksApi

import com.squareup.moshi.Json

data class NetworkImageLinks(
    @Json(name = "thumbnail") val thumbnail: String = "",
    @Json(name = "small") val small: String = ""
)