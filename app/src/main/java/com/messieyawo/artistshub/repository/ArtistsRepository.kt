package com.messieyawo.artistshub.repository

import android.net.Network
import com.messieyawo.artistshub.api.ArtistInterface
import com.messieyawo.artistshub.api.SafeApiRequest

class ArtistsRepository(
    private val api: ArtistInterface
): SafeApiRequest() {
    companion object {
        const val pageSize = 25
    }

   // suspend fun getAllArtist() = apiRequest { api.getAllArtists() }

    suspend fun getAllArtists(offset: Int, topic: String) = apiRequest {
        api.getAllArtistsList(
            offset,
            pageSize,
            topic
        )
    }
    //get genre
    suspend fun getAllGenre() = apiRequest {
        api.getGenre(

        )
    }
    //album repository
    suspend fun getAllAlbums(offset: Int, topic: String) = apiRequest {
        api.getAllAlbums(
            offset,
            pageSize,
            topic
        )
    }
    //get top tracks of a particular artist
    suspend fun getTopTracksOfArtist(id: String) = apiRequest { api.getTopTracksOfArtistById(id) }


    //get user albums
    //get top tracks of a particular artist
    suspend fun getAlbumsOfArtist(id: String) = apiRequest { api.getAlbumOfArtistById(id) }
    suspend fun getAlbumsOfGenre(id: String) = apiRequest { api.getAlbumOfGenreById(id) }



    suspend fun getAllSearchArtists(query:String) =  apiRequest { api.getSearchArtists(query) }

//get tracks of artist album
suspend fun getAlbumsTracksOfArtist(id: String) = apiRequest { api.getAlbumTracksOfArtistById(id) }

    //get genre type
    suspend fun getListGenreById(id: String) = apiRequest {
        api.getGenreById(id)
    }
    //get ghana music
    suspend fun getAllGhanaArtists() = apiRequest {
        api.getGhanaArtists()
    }

    //slider data getTopTracks()

    suspend fun getArtistsTopTracks() = apiRequest {
        api.getTopTracks()
    }

}