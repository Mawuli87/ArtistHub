package com.messieyawo.artistshub.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.databinding.ActivityProfileBinding
import com.messieyawo.artistshub.models.artistAlbums.artistAlbumData
import com.messieyawo.artistshub.models.artists.TopArtistsData
import com.messieyawo.artistshub.models.genreContent.GenreTypeData
import com.messieyawo.artistshub.viewmodel.AllArtistAlbumTracksViewModel
import com.messieyawo.artistshub.viewmodel.ArtistTopTrackViewModel
import com.messieyawo.artistshub.viewmodel.GenreTypeViewModel


class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var artistID:String
    lateinit var artistName:String
    lateinit var artistImg:String

    private val viewmodel by viewModels<ArtistTopTrackViewModel>()
    private val viewodelGenre by viewModels<GenreTypeViewModel>()
    private val viewmodelAlbumtracks by viewModels<AllArtistAlbumTracksViewModel>()

    companion object {
        var model: TopArtistsData? = null
        var albumData: artistAlbumData? = null
        var genreAllData: GenreTypeData? = null

        fun start(c: Context, m: artistAlbumData) {
            albumData = m
            c.startActivity(Intent(c, ProfileActivity::class.java))
        }
        fun start(c: Context, m: TopArtistsData) {
            model = m
            c.startActivity(Intent(c, ProfileActivity::class.java))
        }

        fun start(c: Context, m: GenreTypeData) {
            genreAllData = m
            c.startActivity(Intent(c, ProfileActivity::class.java))
        }

    }
    private var artID:String?=null
    private var artIDGenre:String?=null
    private var artTopID:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        artID =  genreAllData?.id.toString()

        binding.profileArtistName.text =  genreAllData?.name.toString()
        viewmodel.getTopTracks( genreAllData!!.id.toString())

        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))

        Glide.with(this)
            .load( genreAllData?.pictureMedium).into(binding.profileImage)


//        //get artist data from artistActivity intent
//        artistID = intent.getStringExtra("id").toString()
//        artistImg = intent.getStringExtra("img").toString()
//        artistName = intent.getStringExtra("name").toString()
        //set artist name to toolbar


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

       // mNavController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.artistTopFragment,
            R.id.artistAlbumFragment,
            R.id.artistPlayListFragment,
            R.id.userProfileFragment

        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)

    }

    override fun onRestart() {
        super.onRestart()
        viewmodel.getTopTracks(genreAllData!!.id.toString())
    }

    override fun onStart() {
        super.onStart()
             viewmodel.getTopTracks(genreAllData!!.id.toString())

    }


        override fun onResume() {
        super.onResume()
            viewmodel.getTopTracks(genreAllData!!.id.toString())
        (supportActionBar!!.setDisplayHomeAsUpEnabled(false))
    }

    override fun onStop() {
        super.onStop()
       // viewmodel.getTopTracks(model!!.id.toString())
        (supportActionBar!!.setDisplayHomeAsUpEnabled(false))
    }

}