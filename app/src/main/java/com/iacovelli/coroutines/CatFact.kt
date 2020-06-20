package com.iacovelli.coroutines

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatFact(
    val text: String
)