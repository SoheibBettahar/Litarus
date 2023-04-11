package com.example.guttenburg.data.network

import com.squareup.moshi.Json

data class NetworkFormats(
    @Json(name = "application/x-mobipocket-ebook") val applicationXMobitPocketEbook: String?,
    @Json(name = "application/epub+zip") val applicationEpubZip: String?,
    @Json(name = "text/html") val textHtml: String?,
    @Json(name = "application/octet-stream") val applicationOctetStream: String?,
    @Json(name = "image/jpeg") val imageJpeg: String?,
    @Json(name = "text/plain") val textPlain: String?,
    @Json(name = "text/plain; charset=us-ascii") val textPlainCharsetUsAscii: String?,
    @Json(name = "application/rdf+xml") val applicationRdfXml: String?
)



