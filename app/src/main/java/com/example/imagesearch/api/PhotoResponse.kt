package com.example.imagesearch.api

import com.example.imagesearch.data.models.Result

data class PhotoResponse(
    val results: List<Result>
)