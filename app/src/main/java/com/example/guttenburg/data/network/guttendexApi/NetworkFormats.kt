package com.example.guttenburg.data.network.guttendexApi

import com.squareup.moshi.Json

data class NetworkFormats(
    @Json(name = "application/x-mobipocket-ebook") var applicationXMobitPocketEbook: String? = null,
    @Json(name = "application/epub+zip") var applicationEpubZip: String? = null,
    @Json(name = "text/html") var textHtml: String? = null,
    @Json(name = "application/octet-stream") var applicationOctetStream: String? = null,
    @Json(name = "image/jpeg") var imageJpeg: String? = null,
    @Json(name = "text/plain") var textPlain: String? = null,
    @Json(name = "text/plain;charset=us-ascii") var textPlainCharsetUsAscii: String? = null,
    @Json(name = "application/rdf+xml") var applicationRdfXml: String? = null,
    @Json(name = "application/pdf") val applicationPdf: String?
)



