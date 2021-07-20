package com.example.imagesearch.data.models

data class Photo(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
)