package com.messieyawo.artistshub.intro

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AnticipateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewpager2.widget.ViewPager2
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.activities.MainActivity
import com.messieyawo.artistshub.databinding.ActivityWelcomeBinding
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator


class WelcomeActivity : AppCompatActivity() {

    var adapter: OnBoardingAdapter? = null
    var list:ArrayList<OnBoardingIemModel>? = ArrayList()
    lateinit var wormIndicator: WormDotsIndicator
    lateinit var binding:ActivityWelcomeBinding

   // lateinit var videoLayout: VideoLayout
    lateinit var insetsController: WindowInsetsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        installSplashScreen()
        exitWithSlideUp()
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insetsController = window.insetsController!!
            insetsController.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }



        list!!.add(
            OnBoardingIemModel(
            R.drawable.violin,
            "Welcome to Artists Hub.",
            "Find your favorite Artists and music.Go through the world first music database to find what you want."
        )
        )

        list!!.add(
            OnBoardingIemModel(
            R.drawable.dance,
            "Get updated with the Top tracks and Artists",
            "Artist Hub will constantly update the latest information added to the database for you to know the latest in music industry."
        )
        )

        list!!.add(
            OnBoardingIemModel(
            R.drawable.studio,
            "Get and Find your favorite music.",
            "Play preview of all music and know exactly what you are looking for.Unfortunately you won't get the full track here.Maybe later but not for all."
        )
        )


        adapter = OnBoardingAdapter(this,list!!)
        var viewPager = findViewById<ViewPager2>(R.id.view_pager)
        viewPager.adapter = adapter
        wormIndicator = findViewById(R.id.indicator)
        wormIndicator.setViewPager2(viewPager)
        var btnNext = findViewById<Button>(R.id.btn_next)
        var btnSkip = findViewById<TextView>(R.id.tv_skip)
        btnNext.setOnClickListener {
            if (viewPager.currentItem+1 < adapter!!.itemCount){
                viewPager.currentItem +=1
            }else{
                var intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        btnSkip.setOnClickListener {
            var intent = Intent(this@WelcomeActivity,MainActivity::class.java)
            startActivity(intent)
        }

    }



//    private fun hideSystemUI() {
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        WindowInsetsControllerCompat(window, binding.mainContainer).let { controller ->
//            controller.hide(WindowInsetsCompat.Type.systemBars())
//            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
//    }

//    private fun showSystemUI() {
//        WindowCompat.setDecorFitsSystemWindows(window, true)
//        WindowInsetsControllerCompat(window, binding.mainContainer).show(WindowInsetsCompat.Type.systemBars())
//    }


    private fun exitWithSlideUp(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 200L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                slideUp.start()
            }
        }
    }
}