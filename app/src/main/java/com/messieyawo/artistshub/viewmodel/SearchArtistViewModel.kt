package com.messieyawo.artistshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.messieyawo.artistshub.api.ApiException
import com.messieyawo.artistshub.api.ArtistInterface
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.searchArtist.AllArtists
import com.messieyawo.artistshub.repository.ArtistsRepository
import kotlinx.coroutines.launch

class SearchArtistViewModel(application: Application) : AndroidViewModel(application) {

    //all artists
    private val mSearchArtists = MutableLiveData<Event<AllArtists>>()
    /**
     * val artist: LiveData<Event<Artists>>
    private val repository: ArtistsRepository
     * **/
     val searchArtist: LiveData<Event<AllArtists>>
    private val repository: ArtistsRepository = ArtistsRepository(ArtistInterface())

    init {
        mSearchArtists.value = Event.Loading()
        searchArtist = mSearchArtists

    }


    fun getAllSearchArtists(query: String) {

        mSearchArtists.postValue(Event.Loading())

        viewModelScope.launch {
            try {
                val res = repository.getAllSearchArtists(query)

               mSearchArtists.postValue(Event.Success(res))
            } catch (e: Exception) {
                if (e is ApiException) {
                    mSearchArtists.postValue(Event.Error(e.message.toString()))
                } else {
                    mSearchArtists.postValue(Event.Error("No Internet connection."))
                }
            }
        }
    }



}