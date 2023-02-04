package com.messieyawo.artistshub.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.adapters.GenreAdapter
import com.messieyawo.artistshub.databinding.ActivityMainBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.genre.GenreData
import com.messieyawo.artistshub.models.slider.Data
import com.messieyawo.artistshub.music.ghana.GhanaActivity
import com.messieyawo.artistshub.slider.adapter.TravelLocationsAdapter
import com.messieyawo.artistshub.utlis.UtilConstants.RC_APP_UPDATE
import com.messieyawo.artistshub.utlis.UtilConstants.RC_APP_UPDATE_CONTENT
import com.messieyawo.artistshub.viewmodel.GenreTypeViewModel
import com.messieyawo.artistshub.viewmodel.GenreViewModel
import guy4444.smartrate.SmartRate
import kotlin.math.abs


class MainActivity : AppCompatActivity(),TravelLocationsAdapter.itemSlider {

    lateinit var binding: ActivityMainBinding
    private var listSlide = ArrayList<Data>()
    private lateinit var slideAadaper : TravelLocationsAdapter

    private lateinit var adapter: GenreAdapter
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true
    private val list = ArrayList<GenreData>()

    val viewModel: GenreViewModel by viewModels()
    val viewmmodel: GenreTypeViewModel by viewModels()

     //update variable
     lateinit var mAppUpdateManager: AppUpdateManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchData()
        setObserver()
        setRecycler()
        //slider and slider recycler
        fetchSliderData()
        sliderObserver()
        //remind to rate us
        reminderToRateUs()

        binding.cvAlbums.setOnClickListener {
            startActivity(Intent(this,AlbumActivity::class.java))
        }

        binding.searchArtistMain.setOnClickListener {
            startActivity(Intent(this,SearchArtistsActivity::class.java))
        }

         binding.recyclerGenre.setOnClickListener {

         }

        binding.gospelMusic.setOnClickListener {
            val ghanaIntent = Intent(this,GhanaActivity::class.java)
            startActivity(ghanaIntent)
        }

        //in app update initialization
        mAppUpdateManager = AppUpdateManagerFactory.create(this)
        // Before starting an update, register a listener for updates in onCreate() method
        mAppUpdateManager.registerListener(installStateUpdatedListener)

        //in app update

        mAppUpdateManager.appUpdateInfo.addOnSuccessListener { result ->
            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                        result,
                        AppUpdateType.IMMEDIATE,
                        this@MainActivity,
                        RC_APP_UPDATE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun reminderToRateUs() {
        // For continual calls -
        SmartRate.Rate(this
            , "Rate Artists Hub" // title - optional
            , "Tell others what you think about this app" // content - optional
            , "Continue" // OK button text - optional
            , "Ask me later" // later button text - optional
            , "Never ask again" // stop asking button text - optional
            , "Thanks for the feedback" // thanks message to low star users - optional
            , Color.parseColor("#2196F3") // dialog theme color
            , 4 // open google play from _ stars  1..5 - optional
            , 48 // time between calls (unit: Hours) - default is 6 days
            ,
            78 // Time to wait until you start asking for the first time (unit: Hours) - default is 3 days
        )
    }

    private fun fetchData() {
        viewModel.getGenreList()
    }

    private fun setObserver() {
        viewModel.genre.observe(this) {
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startShimmer()
                    else {
                        binding.recyclerMainLoading.visibility= View.VISIBLE
                        binding.recyclerGenre.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false

                    if (it.r.DataGenre.isEmpty()) {
                        allFetched = true
                        binding.recyclerMainLoading.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Seems there are no blogs here.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData()
                            }.show()
                        }
                        return@observe
                    } else {

                        val oldSize = list.size
                        stopShimmer()
                        binding.recyclerMainLoading.visibility = View.GONE
                        binding.recyclerGenre.setPadding(0,0,0,0)
                        list.clear()
                        list.addAll(it.r.DataGenre)
                        Log.d("AllArtistActivity",list.size.toString()+" "+it.r.DataGenre.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.DataGenre.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.recyclerMainLoading.visibility= View.GONE
                    binding.recyclerGenre.setPadding(0,0,0,0)
                    Snackbar.make(binding.root,it.msg, Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        fetchData()
                    }.show()
                }
                else -> {}
            }
        }
    }

    private fun setRecycler() {
        //       var job: Job? = null
//        binding.loadinLayout.visibility = View.VISIBLE
//        job?.cancel()
//        job = MainScope().launch {
//            delay(500L)
//                Glide.with(this@ArtistsActivity).load(R.raw.loading).into(binding.loading)
//
//        }

        Glide.with(this@MainActivity).load(R.raw.loading).into(binding.recyclerMainLoading)
        adapter = GenreAdapter(list)

        binding.recyclerGenre.layoutManager = GridLayoutManager(applicationContext,2)
       // binding.recyclerGenre.layoutManager = LinearLayoutManager(this)
        binding.recyclerGenre.adapter = adapter

    }

    private fun startShimmer() {
        binding.recyclerMainData.visibility = View.GONE
        binding.recyclerMainSkeletonLayout.visibility = View.VISIBLE
        binding.recyclerMainSkeletonLayout.startShimmer()
    }

    private fun stopShimmer() {
        binding.recyclerMainSkeletonLayout.stopShimmer()
        binding.recyclerMainSkeletonLayout.visibility = View.GONE
        binding.recyclerMainData.visibility = View.VISIBLE
    }

    //getArtistsTopTracks()
    private fun fetchSliderData() {
        viewmmodel.getArtistsTopTracks()
    }


    private fun sliderObserver() {
        viewmmodel.slide.observe(this) {
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startSliderShimmer()
                    else {
                       // binding.recyclerMainLoading.visibility= View.VISIBLE
                       // binding.recyclerGenre.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false

                    if (it.r.data.isEmpty()) {
                        allFetched = true
                        Log.i("Slider","${it.r.data.size.toString()}")
                        binding.recyclerMainLoading.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Seems there are no blogs here.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData()
                            }.show()
                        }
                        return@observe
                    } else {

                        val oldSize = listSlide.size
                        stopSliderShimmer()
                       // binding.recyclerMainLoading.visibility = View.GONE
                       // binding.recyclerGenre.setPadding(0,0,0,0)
                        listSlide.clear()
                        listSlide.addAll(it.r.data)
                        binding.locationsViewPager.adapter = TravelLocationsAdapter(listSlide,this)
                         slideAadaper = TravelLocationsAdapter(listSlide,this)
                         slideAadaper.notifyItemRangeInserted(oldSize,it.r.data.size)
                        binding.apply {
                            locationsViewPager.clipToPadding = false
                            locationsViewPager.clipChildren = false
                            locationsViewPager.offscreenPageLimit = 3
                            locationsViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                            val compositePageTransformer = CompositePageTransformer()
                            compositePageTransformer.addTransformer(MarginPageTransformer(40))
                            compositePageTransformer.addTransformer { page, position ->
                                val r: Float = 1 - abs(position)
                                page.scaleY = 0.95f + r * 0.05f
                            }
                            locationsViewPager.setPageTransformer(compositePageTransformer)


                        }
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.recyclerMainLoading.visibility= View.GONE
                    binding.recyclerGenre.setPadding(0,0,0,0)
                    Snackbar.make(binding.root,it.msg, Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        fetchData()
                    }.show()
                }
                else -> {}
            }
        }
    }


    private fun startSliderShimmer() {
        binding.sliderMainLoading.visibility = View.VISIBLE
        Glide.with(this@MainActivity).load(R.raw.loading).into(binding.sliderMainLoading)
    }

    private fun stopSliderShimmer() {
        binding.sliderMainLoading.visibility = View.GONE
    }
//    private fun getLocation(): ArrayList<Data> {
//        val travelLocation = Data()
//
//        listSlide.addAll()
//
//        return listSlide
//    }

//in app update functions
private val installStateUpdatedListener =
    InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            showCompletedUpdate()
        }
    }

    private fun showCompletedUpdate() {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content), RC_APP_UPDATE_CONTENT,
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(
            "Update it "
        ) { mAppUpdateManager.completeUpdate() }
        snackbar.show()
    }


    override fun onResume() {
        super.onResume()
        mAppUpdateManager.appUpdateInfo.addOnSuccessListener { result ->
            if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                        result,
                        AppUpdateType.IMMEDIATE,
                        this@MainActivity,
                        RC_APP_UPDATE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Exit Artists Hub")
            .setIcon(resources.getDrawable(R.drawable.ic_warning))
            .setMessage("Are you sure you want to exit this app.")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes
            ) { _, _ ->
                finishAffinity()
            }.create().show()



    }


    override fun onStop() {
        mAppUpdateManager.unregisterListener(
            installStateUpdatedListener
        )
        super.onStop()
    }

    override fun onItemClicked(position: Int) {
        SliderDetailsActivity.start(this,listSlide[position])
    }


}