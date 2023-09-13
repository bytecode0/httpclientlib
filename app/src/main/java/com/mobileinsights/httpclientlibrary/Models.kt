package com.mobileinsights.httpclientlibrary
data class AmiiboResponse(
    val amiibo: List<AmiiboItem>
)
data class AmiiboItem(
    val amiiboSeries: String,
    val character: String,
    val gameSeries: String,
    val head: String,
    val image: String,
    val name: String,
    val tail: String,
    val type: String
)