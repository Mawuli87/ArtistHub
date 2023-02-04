package com.messieyawo.artistshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.messieyawo.artistshub.api.ApiException
import com.messieyawo.artistshub.api.ArtistInterface
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.album.Album
import com.messieyawo.artistshub.models.artists.TopArtists
import com.messieyawo.artistshub.repository.ArtistsRepository
import kotlinx.coroutines.launch

class ArtistsViewModel(application: Application) : AndroidViewModel(application) {



    //all artists
    private val mArtists = MutableLiveData<Event<TopArtists>>()
    /**
     * val artist: LiveData<Event<Artists>>
    private val repository: ArtistsRepository
     * **/
    val artist: LiveData<Event<TopArtists>>
    private val repository: ArtistsRepository = ArtistsRepository(ArtistInterface())

    //top albums
    private val mAlbums = MutableLiveData<Event<Album>>()
    val albums: LiveData<Event<Album>>
    var oldItems:Album? = null

        init {
        mArtists.value = Event.Loading()
        artist = mArtists

        //top albums
        mAlbums.value = Event.Loading()
        albums = mAlbums

    }


    fun getList(offset: Int, category: String) {
        mArtists.postValue(Event.Loading())

        val c = category



        viewModelScope.launch {
            try {
                val res = repository.getAllArtists(offset, c)

               mArtists.postValue(Event.Success(res))
            } catch (e: Exception) {
                if (e is ApiException) {
                    mArtists.postValue(Event.Error(e.message.toString()))
                } else {
                    mArtists.postValue(Event.Error("No Internet connection."))
                }
            }
        }
    }

    //get top albums getAllAlbums

    fun getListOfAlbums(offset: Int, category: String) {
        mAlbums.postValue(Event.Loading())

        val c = category


        viewModelScope.launch {
            try {
                val res = repository.getAllAlbums(offset, c)
                mAlbums.postValue(Event.Success(res))
            } catch (e: Exception) {
                if (e is ApiException) {
                    mAlbums.postValue(Event.Error(e.message.toString()))
                } else {
                    mAlbums.postValue(Event.Error("No Internet connection."))
                }
            }
        }
    }

}