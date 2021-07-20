package com.example.imagesearch.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.imagesearch.api.ConstantsWebservice
import com.example.imagesearch.data.models.Result
import com.example.imagesearch.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel
@Inject constructor(
    private val photoRepository: PhotoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var resultToShowInFragmentDetails = MutableLiveData<Result>()
    var searchQuery = MutableLiveData<String>()

    private val currentQuery = savedStateHandle.getLiveData(
        ConstantsWebservice.CURRENT_QUERY,
        ConstantsWebservice.DEFAULT_QUERY
    )

    val photos = currentQuery.switchMap { query ->
        photoRepository.searchPhotos(query).cachedIn(viewModelScope)
    }

    fun setQuery(query: String) {
        currentQuery.value = query
    }
}
