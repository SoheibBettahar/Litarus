package com.example.guttenburg.data.network

import android.net.Uri
import com.squareup.moshi.Json

data class NetworkBooksPage(
    @Json(name = "count") val count: Long? = 0,
    @Json(name = "next") private val next: String? = null,
    @Json(name = "previous") private val previous: String? = null,
    @Json(name = "results") val results: List<NetworkBook> = emptyList()
) {


    fun nextPage(): Int? = next?.getPage()
    fun previousPage(): Int? = previous?.getPage()

    private fun String.getPage(): Int? = try {
        val uri = Uri.parse(this)
        uri.getQueryParameter("page")?.toInt()
    } catch (e: java.lang.Exception) {
        null
    }

}




