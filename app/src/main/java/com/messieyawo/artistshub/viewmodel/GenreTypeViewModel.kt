package com.messieyawo.artistshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.messieyawo.artistshub.api.ApiException
import com.messieyawo.artistshub.api.ArtistInterface
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.genreContent.GenreType
import com.messieyawo.artistshub.models.slider.SliderData
import com.messieyawo.artistshub.repository.ArtistsRepository
import kotlinx.coroutines.launch

class GenreTypeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ArtistsRepository = ArtistsRepository(ArtistInterface())

    //all artists
    private val mGenreType = MutableLiveData<Event<GenreType>>()
    /**
     * val artist: LiveData<Event<Artists>>
    private val repository: ArtistsRepository
     * **/
    val genreType: LiveData<Event<GenreType>>

    private val mGhana = MutableLiveData<Event<GenreType>>()
    /**
     * val artist: LiveData<Event<Artists>>
    private val repository: ArtistsRepository
     * **/
    val ghana: LiveData<Event<GenreType>>
    //slider
    private val mSlider = MutableLiveData<Event<SliderData>>()
    val slide: LiveData<Event<SliderData>>






        init {
        mGenreType.value = Event.Loading()
        genreType = mGenreType


            mGhana.value = Event.Loading()
            ghana = mGhana

       //slide data
            mSlider.value = Event.Loading()
            slide = mSlider
        }


    fun getListGenreType(id: String) {
      mGenreType.postValue(Event.Loading())

        viewModelScope.launch {
            try {
                val res = repository.getListGenreById(id)

               mGenreType.postValue(Event.Success(res))
            } catch (e: Exception) {
                if (e is ApiException) {
                    mGenreType.postValue(Event.Error(e.message.toString()))
                } else {
                    mGenreType.postValue(Event.Error("No Internet connection."))
                }
            }
        }
    }

    //getAllGhanaArtists

    fun getAllGhanaArtists() {
        mGhana.postValue(Event.Loading())

        viewModelScope.launch {
            try {
                val res = repository.getAllGhanaArtists()

                mGhana.postValue(Event.Success(res))
            } catch (e: Exception) {
                if (e is ApiException) {
                   mGhana.postValue(Event.Error(e.message.toString()))
                } else {
                    mGhana.postValue(Event.Error("No Internet connection."))
                }
            }
        }
    }


                //slider data getArtistsTopTracks()
                  fun getArtistsTopTracks() {
                    mSlider.postValue(Event.Loading())

                    viewModelScope.launch {
                        try {
                            val res = repository.getArtistsTopTracks()

                            mSlider.postValue(Event.Success(res))
                        } catch (e: Exception) {
                            if (e is ApiException) {
                               mSlider.postValue(Event.Error(e.message.toString()))
                            } else {
                                mSlider.postValue(Event.Error("No Internet connection."))
                            }
                        }
                    }
                }


}