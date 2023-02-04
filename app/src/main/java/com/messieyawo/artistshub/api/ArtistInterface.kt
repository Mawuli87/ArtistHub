package com.messieyawo.artistshub.api

import android.provider.MediaStore
import com.messieyawo.artistshub.models.album.Album
import com.messieyawo.artistshub.models.allArtistAlbumTracks.AllArtistAlbumTracks
import com.messieyawo.artistshub.models.artistAlbums.ArtistAlbums
import com.messieyawo.artistshub.models.artistTopTracks.ArtistTopTracks
import com.messieyawo.artistshub.models.artists.TopArtists
import com.messieyawo.artistshub.models.genre.Genre
import com.messieyawo.artistshub.models.genreContent.GenreType
import com.messieyawo.artistshub.models.searchArtist.AllArtists
import com.messieyawo.artistshub.models.slider.SliderData
import com.messieyawo.artistshub.utlis.UtilConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtistInterface {
    companion object {
        /**
         *Operator Function invoke() Kotlin provides an interesting
         * function called invoke, which is an operator function.
         * Specifying an invoke operator on a class allows it to be
         * called on any instances of the class without a method name
         * **/
        operator fun invoke(): ArtistInterface {
            return RetrofitClient.getClient().create(ArtistInterface::class.java)
        }
    }

//    @GET(UtilConstants.artists)
//    suspend fun getAllArtists(): Response<Artists>


    //get top albums
    @GET(UtilConstants.albums)
    suspend fun getAllAlbums(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("topic") topic: String
    ): Response<Album>

    //get top tracks of an artist
   // @GET(UtilConstants.topSongs+"{id}/tracks")
    //GET("/album/{id}/tracks")
    @GET("/artist/{id}/top")
    suspend fun getTopTracksOfArtistById(@Path("id") id: String): Response<ArtistTopTracks>


    //get user albums
    @GET("/artist/{id}/albums")
    suspend fun getAlbumOfArtistById(@Path("id") id: String): Response<ArtistAlbums>

    //get all genre albums
    @GET("/artist/{id}/albums")
    suspend fun getAlbumOfGenreById(@Path("id") id: String): Response<ArtistAlbums>

    //search for artist
    @GET("/search/artist")
    suspend fun getSearchArtists(
        @Query("q") Query:String
    ):Response<AllArtists>

    //album tracks
    //get user albums
    @GET("/album/{id}/tracks")
    suspend fun getAlbumTracksOfArtistById(@Path("id") id: String):
            Response<AllArtistAlbumTracks>

   //get all artist
   @GET("/chart/0/artists")
   suspend fun getAllArtistsList(
       @Query("offset") offset: Int,
       @Query("limit") limit: Int,
       @Query("topic") topic: String
   ): Response<TopArtists>

    //get genre data
    @GET("/genre")
    suspend fun getGenre(
    ):Response<Genre>

    //genre by id
    //get genre data
    @GET("/genre/{id}/artists")
    suspend fun getGenreById(@Path("id") id: String):Response<GenreType>

    //Ghana musics
    @GET("/search/artist?q=Gospel")
    suspend fun getGhanaArtists(
    ):Response<GenreType>


    @GET("/chart/0/tracks")
    suspend fun getTopTracks():Response<SliderData>


}
