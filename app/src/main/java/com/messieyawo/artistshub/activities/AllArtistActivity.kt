package com.messieyawo.artistshub.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.messieyawo.artistshub.databinding.ActivityAllArtistBinding

class AllArtistActivity : AppCompatActivity() {
    lateinit var binding:ActivityAllArtistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}