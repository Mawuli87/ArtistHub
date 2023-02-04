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
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.activities.SliderDetailsActivity
import com.messieyawo.artistshub.adapters.EachArtistTopTracksAdapter
import com.messieyawo.artistshub.databinding.FragmentSliderPlayListBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.allArtistAlbumTracks.AllArtistAlbumTracksPlayList
import com.messieyawo.artistshub.viewmodel.AllArtistAlbumTracksViewModel


class SliderPlayListFragment : Fragment(),EachArtistTopTracksAdapter.onArtistClick {

    lateinit var binding: FragmentSliderPlayListBinding
    private val viewmodel by viewModels<AllArtistAlbumTracksViewModel>()

    private lateinit var adapter: EachArtistTopTracksAdapter
    private var mediaPlayer: MediaPlayer?= null
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true
    lateinit var tabLayout: TabLayout
    private val list = ArrayList<AllArtistAlbumTracksPlayList>()
    private var artID:String?=null
    private var albname:String?=null
    private var albimg:String?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentSliderPlayListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artID = SliderDetailsActivity.sliderData?.album?.id.toString()
        albname = SliderDetailsActivity.sliderData?.album?.title.toString()
        albimg = SliderDetailsActivity.sliderData?.album?.coverMedium.toString()
        Log.i("ALBUMID",artID.toString())
        //binding.nameText.text = name.toString()


        //  artID = ProfileActivity.albumData?.id.toString()


        // Glide.with(requireActivity()).load(albimg).into(binding.)

        binding.sliderAlbumName.text = albname

        //initialize media player
        mediaPlayer = MediaPlayer()


        artID?.let { fetchData(it) }
        artID?.let { setObserver(it) }
        setRecycler()



        Log.i("ARTISTIDALBUM","Artist ID ID: ${SliderDetailsActivity.sliderData?.album?.id.toString()}")
        Log.i("ARTISTIDALBUM","Album ID ID: ${SliderDetailsActivity.sliderData?.album?.id.toString()}")

    }

    private fun fetchData(artistId: String) {
        isLoading=true

        viewmodel.getAllArtistAlbumsTracks(artistId)
    }

    private fun setObserver(artistId: String) {
        viewmodel.tracks.observe(viewLifecycleOwner) {
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startShimmer()
                    else {
                        binding.sliderEachAlbumListTrackLoading.visibility= View.VISIBLE
                        binding.sliderEachAlbumListRecycler.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false
                    if (it.r.allArtistAlbumData.isEmpty()) {
                        allFetched = true
                        binding.sliderEachAlbumListTrackLoading.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Go to Search to get back your data.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData(artistId)
                            }.show()
                        }
                        return@observe
                    } else {
                        val  totalTrack = it.r.total.toString()
                        if (totalTrack == "1"){
                            binding.sliderTrackNumber.text = "$totalTrack track"
                        }else {
                            binding.sliderTrackNumber.text = "$totalTrack tracks"
                        }
                        val oldSize = list.size
                        stopShimmer()
                        binding.sliderEachAlbumListTrackLoading.visibility = View.GONE
                        binding.sliderEachAlbumListRecycler.setPadding(0,0,0,0)
                        list.clear()
                        list.addAll(it.r.allArtistAlbumData)

                        Log.d("ArtopActivity",list.size.toString()+" "+it.r.allArtistAlbumData.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.allArtistAlbumData.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.sliderEachAlbumListTrackLoading.visibility= View.GONE
                    binding.sliderEachAlbumListRecycler.setPadding(0,0,0,0)
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

        Glide.with(requireActivity()).load(R.raw.loading).into(binding.sliderEachAlbumListTrackLoading)
        adapter = EachArtistTopTracksAdapter(list,this)

        val linearLayoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL ,false)
        binding.sliderEachAlbumListRecycler.layoutManager = linearLayoutManager

        binding.sliderEachAlbumListRecycler.adapter = adapter

//        job = MainScope().launch {
//            delay(500L)
//            binding.loadinLayout.visibility = View.GONE
//
//        }
        binding.sliderEachAlbumListRecycler.addOnScrollListener(scrollListener)
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
        viewmodel.getAllArtistAlbumsTracks(SliderDetailsActivity.sliderData?.album?.id.toString())
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onStop() {
        super.onStop()

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewmodel.getAllArtistAlbumsTracks(SliderDetailsActivity.sliderData?.album?.id.toString())
    }

    private fun startShimmer() {
        binding.sliderSliderEachAlbumListTrackData.visibility = View.GONE
        binding.sliderEachAlbumListTrackSkeletonLayout.visibility = View.VISIBLE
        binding.sliderEachAlbumListTrackSkeletonLayout.startShimmer()
    }

    private fun stopShimmer() {
        binding.sliderEachAlbumListTrackSkeletonLayout.stopShimmer()
        binding.sliderEachAlbumListTrackSkeletonLayout.visibility = View.GONE
        binding.sliderSliderEachAlbumListTrackData.visibility = View.VISIBLE
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

//    override fun fullTrack(pos: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun openTrackLink(link: String) {
//        TODO("Not yet implemented")
//    }


}