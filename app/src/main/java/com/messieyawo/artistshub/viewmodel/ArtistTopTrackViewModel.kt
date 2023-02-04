package com.messieyawo.artistshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.messieyawo.artistshub.api.ApiException
import com.messieyawo.artistshub.api.ArtistInterface
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.artistTopTracks.ArtistTopTracks
import com.messieyawo.artistshub.repository.ArtistsRepository
import kotlinx.coroutines.launch

class ArtistTopTrackViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ArtistsRepository(ArtistInterface())
    private val mTracks = MutableLiveData<Event<ArtistTopTracks>>()
    val tracks: LiveData<Event<ArtistTopTracks>>


init {
    mTracks.value = Event.Loading()
    tracks = mTracks
}


    fun getTopTracks(id: String) {
        mTracks.value = Event.Loading()
        viewModelScope.launch {
            try {
                val r = repository.getTopTracksOfArtist(id)
                mTracks.postValue(Event.Success(r))
            } catch (e: Exception) {
                if (e is ApiException)
                    mTracks.postValue(Event.Error(e.message.toString()))
                else
                    mTracks.postValue(Event.Error("No Internet."))
            }
        }
    }
}