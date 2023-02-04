package com.messieyawo.artistshub.profile.fragmentsAllArtist

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
import com.messieyawo.artistshub.databinding.ActivityAllArtistAlbumsProfileBinding
import com.messieyawo.artistshub.models.artistAlbums.artistAlbumData
import com.messieyawo.artistshub.viewmodel.AllArtistAlbumTracksViewModel


class AllArtistAlbumsProfileActivity : AppCompatActivity() {
    lateinit var binding:ActivityAllArtistAlbumsProfileBinding

    private val viewmodel by viewModels<AllArtistAlbumTracksViewModel>()

    companion object {
        //require search result of artist to get album of a particular artist
        var modelAlbum: artistAlbumData? = null


        fun start(c: Context, ms: artistAlbumData) {
            modelAlbum = ms
            c.startActivity(Intent(c, AllArtistAlbumsProfileActivity::class.java))
        }


    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllArtistAlbumsProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)



        //set artist name to toolbar
        binding.allArtistProfileName.text = modelAlbum?.title.toString()
        Glide.with(this).load(modelAlbum?.coverMedium).into(binding.allArtistProfileImage)

        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.all_artist_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.allArtistTopTracksFragment,
//            R.id.allArtistAlbumPlayListFragment,

        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.AllArtistBottomNavigationView.setupWithNavController(navController)

    }

    override fun onResume() {
        super.onResume()
        viewmodel.getAllArtistAlbumsTracks(modelAlbum?.id.toString())
    }

    override fun onStop() {
        super.onStop()
        viewmodel.getAllArtistAlbumsTracks(modelAlbum?.id.toString())
    }

    override fun onStart() {
        super.onStart()
        viewmodel.getAllArtistAlbumsTracks(modelAlbum!!.id.toString())

    }



}