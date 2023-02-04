package com.messieyawo.artistshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.messieyawo.artistshub.api.ArtistInterface
import com.messieyawo.artistshub.repository.ArtistsRepository

class TrendingArtistsViewModel(application: Application) : AndroidViewModel(application) {

  //call repository here
    private val repository = ArtistsRepository(ArtistInterface())

//    private val _trendingArtist = MutableLiveData<Event<ArrayList<ArtistsData>>>()
//    val trendingArtist: LiveData<Event<ArrayList<ArtistsData>>>
//
//    init {
//
//        _trendingArtist.value = Event.Loading()
//        trendingArtist =  _trendingArtist
//    }

//    fun getAllArtist() {
//        viewModelScope.launch {
//            try {
//               // val res = repository.getAllArtist()
//                if (res.data.isNullOrEmpty())
//                    return@launch
//                _trendingArtist.postValue(Event.Success(res.data,""))
//            } catch (e:Exception) {
//                if (e is ApiException)
//                    _trendingArtist.postValue(Event.Error(e.message.toString()))
//                else
//                    _trendingArtist.postValue(Event.Error("No Internet."))
//            }
//        }
//    }


}