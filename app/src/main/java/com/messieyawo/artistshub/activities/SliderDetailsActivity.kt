package com.messieyawo.artistshub.activities

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
import com.messieyawo.artistshub.databinding.ActivitySliderDetailsBinding
import com.messieyawo.artistshub.models.slider.Data
import com.messieyawo.artistshub.viewmodel.GenreTypeViewModel

class SliderDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivitySliderDetailsBinding
    val viewmmodel: GenreTypeViewModel by viewModels()

    companion object {

        var sliderData: Data? = null

        fun start(c: Context, m: Data) {
            sliderData = m
            c.startActivity(Intent(c, SliderDetailsActivity::class.java))
        }
    }
    private var artID:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySliderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.sliderToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        artID =  sliderData?.id.toString()
        binding.sliderProfileArtistName.text =  sliderData?.artist?.name.toString()
       // viewmmodel.getTopTracks( SliderDetailsActivity.genreAllData!!.id.toString())

        binding.sliderToolbar.setTitleTextColor(resources.getColor(R.color.white))

        Glide.with(this)
            .load(sliderData?.artist?.pictureMedium).into(binding.sliderProfileImage)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.slider_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // mNavController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.sliderToptracksFragment,
            R.id.sliderAlbumsFragment,
            R.id.sliderPlayListFragment,

        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.sliderBottomNavigationView.setupWithNavController(navController)
    }




}