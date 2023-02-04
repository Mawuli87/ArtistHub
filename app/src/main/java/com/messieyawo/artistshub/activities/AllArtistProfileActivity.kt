package com.messieyawo.artistshub.activities


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.adapters.AllProfileArtistAdapter
import com.messieyawo.artistshub.databinding.ActivityAllArtistProfileBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.artistAlbums.artistAlbumData
import com.messieyawo.artistshub.models.searchArtist.SearchData
import com.messieyawo.artistshub.viewmodel.ArtistAlbumsViewModel

class AllArtistProfileActivity : AppCompatActivity(),AllProfileArtistAdapter.onArtistClick {
    lateinit var binding:ActivityAllArtistProfileBinding

    private val viewModel by viewModels<ArtistAlbumsViewModel>()
    private lateinit var adapter: AllProfileArtistAdapter
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true
    private val list = ArrayList<artistAlbumData>()

    companion object {
        //require search result of artist to get album of a particular artist
        var modelSearch: SearchData? = null
        var modelAlbum: artistAlbumData? = null


        fun start(c: Context, ms: SearchData) {
            modelSearch = ms
            c.startActivity(Intent(c, AllArtistProfileActivity::class.java))
        }

//         //geting album data from AllProfileAdapter
//        fun start(c: Context, mal: artistAlbumData) {
//            modelAlbum = mal
//            c.startActivity(Intent(c, AllArtistProfileActivity::class.java))
//        }
    }

//    private var artistID:String? = null
//    private var  artistImg:String? = null
//    private var  artistName:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllArtistProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


//               //get artist data from artistActivity intent
//         artistID = intent.getStringExtra("id").toString()
//         artistImg = intent.getStringExtra("img").toString()
//         artistName = intent.getStringExtra("name").toString()

        binding.allArtistAppBarText.text = modelSearch?.name.toString()
        Glide.with(this)
            .load(modelSearch?.pictureMedium).into(binding.allArtistUserImage)


        fetchData()
        setObserver()
        setRecycler()



    }
    private fun fetchData() {
        isLoading=true

        viewModel.getAlbums(modelSearch?.id.toString())
    }

    private fun setObserver() {
        viewModel.albums.observe(this) {
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startShimmer()
                    else {
                        binding.allArtistLoading.visibility= View.VISIBLE
                        binding.allArtistBlogRecycler.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false
                    if (it.r.artistAlbumData.isNullOrEmpty()) {
                        allFetched = true
                        binding.allArtistLoading.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Please", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData()
                            }.show()
                        }
                        return@observe
                    } else {
                        val oldSize = list.size
                        stopShimmer()
                        binding.allArtistLoading.visibility = View.GONE
                        binding.allArtistBlogRecycler.setPadding(0,0,0,0)
                        list.clear()
                        list.addAll(it.r.artistAlbumData)
                        Log.d("Category Activity",list.size.toString()+" "+it.r.artistAlbumData.size.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.artistAlbumData.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.allArtistLoading.visibility= View.GONE
                    binding.allArtistBlogRecycler.setPadding(0,0,0,0)
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

        Glide.with(this@AllArtistProfileActivity).load(R.raw.loading).into(binding.allArtistLoading)
        adapter = AllProfileArtistAdapter(list,this)

        binding.allArtistBlogRecycler.layoutManager = LinearLayoutManager(this)
        binding.allArtistBlogRecycler.adapter = adapter

//        job = MainScope().launch {
//            delay(500L)
//            binding.loadinLayout.visibility = View.GONE
//
//        }
        binding.allArtistBlogRecycler.addOnScrollListener(scrollListener)
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
                fetchData()
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

    private fun startShimmer() {
        binding.allArtistData.visibility = View.GONE
        binding.allArtistSkeletonLayout.visibility = View.VISIBLE
        binding.allArtistSkeletonLayout.startShimmer()
    }

    private fun stopShimmer() {
        binding.allArtistSkeletonLayout.stopShimmer()
        binding.allArtistSkeletonLayout.visibility = View.GONE
        binding.allArtistData.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
       viewModel.getAlbums(modelSearch?.id.toString())
       // viewModel.getAlbums(AllArtistAlbumsProfileActivity.modelAlbum?.id.toString())

    }

    override fun viewLink(link: String) {
        val data = link
        val defaultBrowser =
            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
        defaultBrowser.data = Uri.parse(data)
        startActivity(defaultBrowser)
//        val intent = Intent(this,WebClientActivity::class.java)
//        intent.putExtra("link",link)
//        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        viewModel.getAlbums(modelSearch?.id.toString())
    }

}