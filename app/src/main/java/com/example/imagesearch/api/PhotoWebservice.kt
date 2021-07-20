package com.example.imagesearch.api

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PhotoWebservice {
    @Headers(
        ConstantsWebservice.ACCEPT_VERSION,
        ConstantsWebservice.AUTHORISATION_CLIENT_ID + ConstantsWebservice.SPACE + ConstantsWebservice.API_KEY
    )
    @GET(ConstantsWebservice.SEARCH_PHOTOS)
    suspend fun searchPhotos(
        @Query(ConstantsWebservice.QUERY) query: String,
        @Query(ConstantsWebservice.PAGE) page: Int,
        @Query(ConstantsWebservice.PER_PAGE) perPage: Int
    ): PhotoResponse
}