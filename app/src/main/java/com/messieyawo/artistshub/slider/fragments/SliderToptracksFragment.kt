package com.messieyawo.artistshub.slider.fragments

import android.app.AlertDialog
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.activities.SliderDetailsActivity
import com.messieyawo.artistshub.adapters.ArtistTopTracksAdapter
import com.messieyawo.artistshub.databinding.FragmentSliderToptracksBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.artistTopTracks.Data
import com.messieyawo.artistshub.viewmodel.ArtistTopTrackViewModel


class SliderToptracksFragment : Fragment(R.layout.fragment_slider_toptracks),ArtistTopTracksAdapter.onArtistClick {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    private var mediaPlayer: MediaPlayer?= null
    private val viewmodel by viewModels<ArtistTopTrackViewModel>()
    private lateinit var adapter: ArtistTopTracksAdapter
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true
    private val list = ArrayList<Data>()
    private var artID:String?=null
    lateinit var binding:FragmentSliderToptracksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentSliderToptracksBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = binding.root.findViewById(R.id.slider_tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText("Top Artist tracks"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        artID =  SliderDetailsActivity.sliderData?.artist?.id.toString()
        //initialize media player
        mediaPlayer = MediaPlayer()
        artID?.let { fetchData(it) }
        artID?.let { setObserver(it) }
        setRecycler()

        Log.i("ARTISTID","Artist ID: ${ SliderDetailsActivity.sliderData?.artist?.id.toString()}")
    }


    private fun fetchData(artistId: String) {
        isLoading=true

        viewmodel.getTopTracks(artistId)
    }

    /**
     * fetch single artist top tracks
     * **/

    private fun setObserver(artistId: String) {
        viewmodel.tracks.observe(viewLifecycleOwner) {
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startShimmer()
                    else {
                        binding.sliderArtTopTrackLoading.visibility= View.VISIBLE
                        binding.sliderArtTopTrackBlogRecycler.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false
                    if (it.r.ArtisTopTrackdata.isEmpty()) {
                        allFetched = true
                        binding.sliderArtTopTrackLoading.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Seems there are no data here.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData(artistId)
                            }.show()
                        }
                        return@observe
                    } else {
                        val oldSize = list.size
                        stopShimmer()
                        binding.sliderArtTopTrackLoading.visibility = View.GONE
                        binding.sliderArtTopTrackBlogRecycler.setPadding(0,0,0,0)
                        // list.clear()
                        list.addAll(it.r.ArtisTopTrackdata)

                        Log.d("ArtopActivity",list.size.toString()+" "+it.r.ArtisTopTrackdata.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.ArtisTopTrackdata.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.sliderArtTopTrackLoading.visibility= View.GONE
                    binding.sliderArtTopTrackBlogRecycler.setPadding(0,0,0,0)
                    Snackbar.make(binding.root,it.msg, Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        fetchData(artistId)
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

        Glide.with(requireActivity()).load(R.raw.loading).into(binding.sliderArtTopTrackLoading)
        adapter = ArtistTopTracksAdapter(list,this)

        val linearLayoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL ,false)
        binding.sliderArtTopTrackBlogRecycler.layoutManager = linearLayoutManager

        binding.sliderArtTopTrackBlogRecycler.adapter = adapter

//        job = MainScope().launch {
//            delay(500L)
//            binding.loadinLayout.visibility = View.GONE
//
//        }
        binding.sliderArtTopTrackBlogRecycler.addOnScrollListener(scrollListener)
    }
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isScrolling

            if (shouldPaginate) {
                fetchData(SliderDetailsActivity.sliderData?.album?.id.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewmodel.getTopTracks(SliderDetailsActivity.sliderData?.artist?.id.toString())
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onStop() {
        super.onStop()

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewmodel.getTopTracks(SliderDetailsActivity.sliderData?.artist?.id.toString())
    }

    private fun startShimmer() {
        binding.sliderArtTopTrackData.visibility = View.GONE
        binding.sliderArtTopTrackSkeletonLayout.visibility = View.VISIBLE
        binding.sliderArtTopTrackSkeletonLayout.startShimmer()
    }

    private fun stopShimmer() {
        binding.sliderArtTopTrackSkeletonLayout.stopShimmer()
        binding.sliderArtTopTrackSkeletonLayout.visibility = View.GONE
        binding.sliderArtTopTrackData.visibility = View.VISIBLE
    }

    override fun artistClick(title: String,picture:String,link:String) {
        //val dialog = setProgressDialog(requireActivity(), "Streaming $title..")
        // dialog.show()
        //val mp3Link = pos
        // Log.i("MP3","MP3LINK $mp3Link")
        val builder = AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.customview_layout, null)
        val title1: TextView = view.findViewById(R.id.titleDialog)
        //progress bar
        val pgrbar: ProgressBar = view.findViewById(R.id.progress_bar)

        //val link1: TextView = view.findViewById(R.id.linkboxTextView)
        title1.text = title
        // link1.text = picture
        //  val description: TextView = view.findViewById(R.id.descriptionDialog)
        val posterImg: ImageView = view.findViewById(R.id.image_slider_single)

        Glide.with(requireActivity())
            .load(picture).into(posterImg)
        val imageViewPlay = view.findViewById<ShapeableImageView>(R.id.play_top_track_loading)
        val imageViewPause = view.findViewById<ShapeableImageView>(R.id.pause_top_track_loading)



        imageViewPlay.setOnClickListener {
            //  playAudio(link)
            //  dialog.show()
            imageViewPlay.visibility = View.GONE
            // imageViewPause.visibility = View.VISIBLE
            pgrbar.visibility = View.VISIBLE



            // initializing media player

            Log.i("LOGLINK","$link")



            val url = link // your URL here
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                prepare() // might take long! (for buffering, etc)
                start()
                imageViewPause.visibility = View.VISIBLE
                pgrbar.visibility = View.GONE

            }


            // dialog.dismiss()
            Toast.makeText(requireActivity(), "Audio started playing..", Toast.LENGTH_SHORT).show()


        }

        imageViewPause.setOnClickListener {
            if (mediaPlayer!!.isPlaying) {

                mediaPlayer?.release()
                mediaPlayer = null

                imageViewPause.visibility = View.GONE
                imageViewPlay.visibility = View.VISIBLE
                // below line is to display a message
                // when media player is paused.

                Toast.makeText(requireActivity(), "Audio has been stopped", Toast.LENGTH_LONG).show()
            } else {
                // this method is called when media
                // player is not playing.

                Toast.makeText(requireActivity(), "Please close and play again", Toast.LENGTH_LONG).show()
            }
            // pauseMusic()
        }

        val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
        builder.setView(view)
        button.setOnClickListener {
            if (mediaPlayer == null){

                builder.dismiss()
            }else {
                mediaPlayer?.release()
                mediaPlayer = null
            }

            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }




}