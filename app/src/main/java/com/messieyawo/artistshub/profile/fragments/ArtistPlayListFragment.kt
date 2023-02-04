package com.messieyawo.artistshub.profile.fragments

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
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
import com.messieyawo.artistshub.adapters.EachArtistTopTracksAdapter
import com.messieyawo.artistshub.databinding.FragmentArtistPlayListBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.allArtistAlbumTracks.AllArtistAlbumTracksPlayList
import com.messieyawo.artistshub.profile.ProfileActivity
import com.messieyawo.artistshub.viewmodel.AllArtistAlbumTracksViewModel


class ArtistPlayListFragment : Fragment(R.layout.fragment_artist_play_list) ,EachArtistTopTracksAdapter.onArtistClick{
lateinit var binding:FragmentArtistPlayListBinding
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



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentArtistPlayListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       artID = arguments?.getString("id")
       albname = arguments?.getString("title")
       albimg = arguments?.getString("img")
        Log.i("ALBUMID",artID.toString())
        //binding.nameText.text = name.toString()


      //  artID = ProfileActivity.albumData?.id.toString()


      // Glide.with(requireActivity()).load(albimg).into(binding.)

        binding.albumName.text = albname

       //initialize media player
        mediaPlayer = MediaPlayer()


        artID?.let { fetchData(it) }
        artID?.let { setObserver(it) }
        setRecycler()
        setRecycler()


        Log.i("ARTISTIDALBUM","Artist ID ID: ${ProfileActivity.albumData?.id.toString()}")
        Log.i("ARTISTIDALBUM","Album ID ID: ${ProfileActivity.model?.id.toString()}")

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
                        binding.eachAlbumListTrackLoading.visibility= View.VISIBLE
                        binding.eachAlbumListRecycler.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false
                    if (it.r.allArtistAlbumData.isNullOrEmpty()) {
                        allFetched = true
                        binding.eachAlbumListTrackLoading.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Go to Search to get back your data.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData(artistId)
                            }.show()
                        }
                        return@observe
                    } else {
                       val  totalTrack = it.r.total.toString()
                         if (totalTrack == "1"){
                             binding.trackNumber.text = "$totalTrack track"
                         }else {
                             binding.trackNumber.text = "$totalTrack tracks"
                         }
                        val oldSize = list.size
                        stopShimmer()
                        binding.eachAlbumListTrackLoading.visibility = View.GONE
                        binding.eachAlbumListRecycler.setPadding(0,0,0,0)
                         list.clear()
                        list.addAll(it.r.allArtistAlbumData)

                        Log.d("ArtopActivity",list.size.toString()+" "+it.r.allArtistAlbumData.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.allArtistAlbumData.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.eachAlbumListTrackLoading.visibility= View.GONE
                    binding.eachAlbumListRecycler.setPadding(0,0,0,0)
                    Snackbar.make(binding.root,it.msg, Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        fetchData(artistId)
                    }.show()
                }
                else -> {}
            }
        }
    }



    private fun startShimmer() {
        binding.eachAlbumListTrackData.visibility = View.GONE
        binding.eachAlbumListTrackSkeletonLayout.visibility = View.VISIBLE
        binding.eachAlbumListTrackSkeletonLayout.startShimmer()
    }

    private fun stopShimmer() {
        binding.eachAlbumListTrackSkeletonLayout.stopShimmer()
        binding.eachAlbumListTrackSkeletonLayout.visibility = View.GONE
        binding.eachAlbumListTrackData.visibility = View.VISIBLE
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        viewmodel.getAllArtistAlbumsTracks(artID.toString())
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        viewmodel.getAllArtistAlbumsTracks(artID.toString())
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

        Glide.with(requireActivity()).load(albimg).into(binding.eachAlbumListTrackLoading)
        adapter = EachArtistTopTracksAdapter(list,this)

        val linearLayoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL ,false)
        binding.eachAlbumListRecycler.layoutManager = linearLayoutManager

        binding.eachAlbumListRecycler.adapter = adapter

//        job = MainScope().launch {
//            delay(500L)
//            binding.loadinLayout.visibility = View.GONE
//
//        }
        binding.eachAlbumListRecycler.addOnScrollListener(scrollListener)
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
                fetchData(ProfileActivity.albumData?.id.toString())
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



    override fun artistClick(title: String,picture:String,link:String) {
//        val builder1 = AlertDialog.Builder(context)
//        builder1.setCancelable(false)
//        builder1.setTitle("Preview $title ")// if you want user to wait for some process to finish,
//        builder1.setMessage("Please wait for a moment...")
//        val dialog = builder1.create()
        //val mp3Link = pos
        // Log.i("MP3","MP3LINK $mp3Link")
        val builder = AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.customview_layout, null)
        val title1: TextView = view.findViewById(R.id.titleDialog)
        //val link1: TextView = view.findViewById(R.id.linkboxTextView)
        title1.text = title
        // link1.text = picture
        //  val description: TextView = view.findViewById(R.id.descriptionDialog)
        val posterImg: ImageView = view.findViewById(R.id.image_slider_single)

        Glide.with(requireActivity())
            .load(albimg).into(posterImg)
        val imageViewPlay = view.findViewById<ShapeableImageView>(R.id.play_top_track_loading)
        val imageViewPause = view.findViewById<ShapeableImageView>(R.id.pause_top_track_loading)



        imageViewPlay.setOnClickListener {
            //  playAudio(link)

            imageViewPlay.visibility = View.GONE
            imageViewPause.visibility = View.VISIBLE



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
            }

//             // below line is use to set the audio
//             // stream type for our media player.
//
//                 mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
//                // mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
//
//
//             // below line is use to set our
//             // url to our media player.
//
//             try {
//                 mediaPlayer?.setDataSource(link)
//                 // below line is use to prepare
//                 // and start our media player.
//
//                 mediaPlayer?.prepare()
//
//                 mediaPlayer?.start()
//             } catch (e: IOException) {
//                 e.printStackTrace()
//
//             }
//             // below line is use to display a toast message.

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
//        val data = pos
//        val defaultBrowser =
//            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
//        defaultBrowser.data = Uri.parse(data)
//        startActivity(defaultBrowser)
//    }
//
//    override fun openTrackLink(link: String) {
//        val data = link
//        val defaultBrowser =
//            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
//        defaultBrowser.data = Uri.parse(data)
//        startActivity(defaultBrowser)
//
//
//    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewmodel.getAllArtistAlbumsTracks(artID.toString())
    }


    override fun onStart() {
        super.onStart()
        viewmodel.getAllArtistAlbumsTracks(artID.toString())

    }

}
