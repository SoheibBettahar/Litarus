package com.example.guttenburg.data.network

import com.squareup.moshi.Json

data class NetworkPerson(
    @Json(name = "name") var name: String? = null,
    @Json(name = "birth_year") var birthYear: Int? = null,
    @Json(name = "death_year") var deathYear: Int? = null
)