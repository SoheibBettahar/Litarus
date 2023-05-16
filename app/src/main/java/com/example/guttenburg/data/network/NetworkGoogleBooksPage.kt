package com.example.guttenburg.data.network

import com.squareup.moshi.Json

data class NetworkGoogleBookPage(@Json(name = "items") val items: List<NetworkGoogleBook> = emptyList())

data class NetworkGoogleBook(
    @Json(name = "volumeInfo") val bookInfo: NetworkGoogleBookInfo? = null,
)

data class NetworkGoogleBookInfo(
    @Json(name = "title") var title: String? = null,
    @Json(name = "subtitle") var subtitle: String? = null,
    @Json(name = "authors") var authors: List<String> = emptyList(),
    @Json(name = "description") var description: String? = null,
    @Json(name = "pageCount") var pageCount: Int? = null,
    //@Json(name = "publisher") var publisher: String? = null,
    //@Json(name = "publishedDate") var publishedDate: String? = null,
    //@Json(name = "industryIdentifiers") var industryIdentifiers: ArrayList<IndustryIdentifiers> = arrayListOf(),
    //@Json(name = "readingModes") var readingModes: ReadingModes? = ReadingModes(),
    //@Json(name = "printType") var printType: String? = null,
    //@Json(name = "categories") var categories: ArrayList<String> = arrayListOf(),
    //@Json(name = "averageRating") var averageRating: Int? = null
    //@Json(name = "ratingsCount") var ratingsCount: Int? = null,
    //@Json(name = "maturityRating") var maturityRating: String? = null,
    //@Json(name = "allowAnonLogging") var allowAnonLogging: Boolean? = null,
    //@Json(name = "contentVersion") var contentVersion: String? = null,
    //@Json(name = "panelizationSummary") var panelizationSummary: PanelizationSummary? = PanelizationSummary(),
    //@Json(name = "imageLinks") var imageLinks: ImageLinks? = ImageLinks(),
    //@Json(name = "language") var language: String? = null,
    //@Json(name = "previewLink") var previewLink: String? = null,
    //@Json(name = "infoLink") var infoLink: String? = null,
    //@Json(name = "canonicalVolumeLink") var canonicalVolumeLink: String? = null
)