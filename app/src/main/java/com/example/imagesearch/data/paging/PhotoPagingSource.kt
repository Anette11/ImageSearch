package com.example.imagesearch.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imagesearch.api.ConstantsWebservice
import com.example.imagesearch.api.PhotoWebservice
import com.example.imagesearch.data.models.Result
import retrofit2.HttpException
import java.io.IOException

class PhotoPagingSource(
    private val photoWebservice: PhotoWebservice,
    private val query: String
) : PagingSource<Int, Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val nextPageNumber = params.key ?: ConstantsWebservice.START_PAGE_INDEX
            val photoResponse = photoWebservice.searchPhotos(query, nextPageNumber, params.loadSize)
            val listOfResults = photoResponse.results

            LoadResult.Page(
                data = listOfResults,
                prevKey = if (nextPageNumber == ConstantsWebservice.START_PAGE_INDEX) null
                else nextPageNumber.minus(1),
                nextKey = if (listOfResults.isEmpty()) null
                else nextPageNumber.plus(1)
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}