package com.example.imagesearch.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.imagesearch.api.ConstantsWebservice
import com.example.imagesearch.api.PhotoWebservice
import com.example.imagesearch.data.paging.PhotoPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository
@Inject constructor(
    private val photoWebservice: PhotoWebservice
) {
    fun searchPhotos(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = ConstantsWebservice.PAGE_SIZE,
                maxSize = ConstantsWebservice.MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PhotoPagingSource(photoWebservice, query)
            }
        ).liveData
}