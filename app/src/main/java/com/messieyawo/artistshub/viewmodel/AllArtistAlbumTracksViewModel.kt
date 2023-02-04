package com.messieyawo.artistshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.messieyawo.artistshub.api.ApiException
import com.messieyawo.artistshub.api.ArtistInterface
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.allArtistAlbumTracks.AllArtistAlbumTracks
import com.messieyawo.artistshub.repository.ArtistsRepository
import kotlinx.coroutines.launch

class AllArtistAlbumTracksViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ArtistsRepository(ArtistInterface())
    private val mTracks = MutableLiveData<Event<AllArtistAlbumTracks>>()
    val tracks: LiveData<Event<AllArtistAlbumTracks>>


    init {
        mTracks.value = Event.Loading()
        tracks = mTracks
    }


    fun getAllArtistAlbumsTracks(id: String) {
        mTracks.value = Event.Loading()
        viewModelScope.launch {
            try {
                val r = repository.getAlbumsTracksOfArtist(id)
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