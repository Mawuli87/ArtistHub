package com.messieyawo.artistshub.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.adapters.AlbumAdapter
import com.messieyawo.artistshub.databinding.ActivityAlbumBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.album.AlbumsData
import com.messieyawo.artistshub.viewmodel.ArtistsViewModel
import de.hdodenhof.circleimageview.CircleImageView

class AlbumActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlbumBinding

    companion object {
        var category = ""

    }
    //same viewmodel for artists and albums
    private val viewModel by viewModels<ArtistsViewModel>()
    private lateinit var adapter: AlbumAdapter
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true
    private val list = ArrayList<AlbumsData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAppBar()
        fetchAlbumData()
        setObserver()
        setRecycler()
    }

    private fun fetchAlbumData() {
        isLoading=true
        viewModel.getListOfAlbums(list.size, category)
    }
//    private fun fetchData() {
//        isLoading=true
//
//        viewModel.getListOfAlbums(list.size, category)
//    }

    private fun setObserver() {
        viewModel.albums.observe(this) {
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startShimmer()
                    else {
                        binding.loadingAlbums.visibility= View.VISIBLE
                        binding.albumRecycler.setPadding(0,0,0,30)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false
                    if (it.r.AlbumsData.isEmpty()) {
                        allFetched = true
                        binding.loadingAlbums.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Seems there are no albums here.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                               // fetchData()
                                fetchAlbumData()
                            }.show()
                        }
                        return@observe
                    } else {
                        val oldSize = list.size
                        stopShimmer()
                        binding.loadingAlbums.visibility = View.GONE
                        binding.albumRecycler.setPadding(0,0,0,0)
                         list.clear()
                         list.addAll(it.r.AlbumsData)

                        Log.i("AlbumActivity",list.size.toString()+" "+it.r.AlbumsData.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.AlbumsData.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.loadingAlbums.visibility= View.GONE
                    binding.albumRecycler.setPadding(0,0,0,20)
                    Snackbar.make(binding.root,it.msg, Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                       // fetchData()
                        fetchAlbumData()
                    }.show()
                }
                else -> {}
            }
        }
    }

    private fun startShimmer() {
        binding.data.visibility = View.GONE
        binding.skeletonLayoutAlbums.visibility = View.VISIBLE
        binding.skeletonLayoutAlbums.startShimmer()
    }
    private fun stopShimmer() {
        binding.skeletonLayoutAlbums.stopShimmer()
        binding.skeletonLayoutAlbums.visibility = View.GONE
        binding.data.visibility = View.VISIBLE
    }

    private fun setAppBar() {
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.app_bar)
        supportActionBar?.customView?.apply {
            findViewById<TextView>(R.id.app_bar_text).text = ArtistsActivity.category
            findViewById<CircleImageView>(R.id.user_image).setImageResource(R.color.white_ex)
        }
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.white_ex))
    }

    private fun setRecycler() {

        Glide.with(this@AlbumActivity).load(R.raw.loading).into(binding.loadingAlbums)
        adapter = AlbumAdapter(list)

        binding.albumRecycler.layoutManager = LinearLayoutManager(this)
        binding.albumRecycler.adapter = adapter


        binding.albumRecycler.addOnScrollListener(scrollListener)
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
               // fetchData()
                fetchAlbumData()
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
}