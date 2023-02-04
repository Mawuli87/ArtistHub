package com.messieyawo.artistshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.messieyawo.artistshub.api.ApiException
import com.messieyawo.artistshub.api.ArtistInterface
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.artistAlbums.ArtistAlbums
import com.messieyawo.artistshub.repository.ArtistsRepository
import kotlinx.coroutines.launch

class ArtistAlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ArtistsRepository(ArtistInterface())
    private val mAlbums = MutableLiveData<Event<ArtistAlbums>>()
    val albums: LiveData<Event<ArtistAlbums>>



init {
    mAlbums.value = Event.Loading()
    albums = mAlbums
}


    fun getAlbumsOfGenre(id: String) {
        mAlbums.value = Event.Loading()
        viewModelScope.launch {
            try {
                val r = repository.getAlbumsOfArtist(id)
                mAlbums.postValue(Event.Success(r))
            } catch (e: Exception) {
                if (e is ApiException)
                    mAlbums.postValue(Event.Error(e.message.toString()))
                else
                    mAlbums.postValue(Event.Error("No Internet."))
            }
        }
    }



    fun getAlbums(id: String) {
        mAlbums.value = Event.Loading()
        viewModelScope.launch {
            try {
                val r = repository.getAlbumsOfArtist(id)
                mAlbums.postValue(Event.Success(r))
            } catch (e: Exception) {
                if (e is ApiException)
                    mAlbums.postValue(Event.Error(e.message.toString()))
                else
                    mAlbums.postValue(Event.Error("No Internet."))
            }
        }
    }



}