package com.messieyawo.artistshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.messieyawo.artistshub.api.ApiException
import com.messieyawo.artistshub.api.ArtistInterface
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.genre.Genre
import com.messieyawo.artistshub.repository.ArtistsRepository
import kotlinx.coroutines.launch

class GenreViewModel(application: Application) : AndroidViewModel(application) {

    //all artists
    private val mGenre = MutableLiveData<Event<Genre>>()
    /**
     * val artist: LiveData<Event<Artists>>
    private val repository: ArtistsRepository
     * **/
    val genre: LiveData<Event<Genre>>
    private val repository: ArtistsRepository = ArtistsRepository(ArtistInterface())

    init {
        mGenre.value = Event.Loading()
        genre = mGenre

    }

    fun getGenreList() {
        mGenre.postValue(Event.Loading())
        viewModelScope.launch {
            try {
                val res = repository.getAllGenre()

                mGenre.postValue(Event.Success(res))
            } catch (e: Exception) {
                if (e is ApiException) {
                    mGenre.postValue(Event.Error(e.message.toString()))
                } else {
                    mGenre.postValue(Event.Error("No Internet connection."))
                }
            }
        }
    }
}