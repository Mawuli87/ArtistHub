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
import com.messieyawo.artistshub.adapters.ArtistAdapter
import com.messieyawo.artistshub.databinding.ActivityArtistsBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.artists.TopArtistsData
import com.messieyawo.artistshub.viewmodel.ArtistsViewModel
import de.hdodenhof.circleimageview.CircleImageView

class ArtistsActivity : AppCompatActivity() {
    lateinit var binding: ActivityArtistsBinding

    companion object {
        var category = ""


    }

    private val viewModel by viewModels<ArtistsViewModel>()
    private lateinit var adapter: ArtistAdapter
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true
    private val list = ArrayList<TopArtistsData>()
    var next:String?=null
    var totalPage:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAppBar()
        setRecycler()
        setObserver()
        fetchData()

    }

    private fun fetchData() {
        isLoading=true

        viewModel.getList(list.size, category)
    }

    private fun setObserver() {
        viewModel.artist.observe(this) {
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startShimmer()
                    else {
                        binding.loading.visibility= View.VISIBLE
                        binding.blogRecycler.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false

                    if (it.r.topArtData.isEmpty()) {
                        allFetched = true
                        binding.loading.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Seems there are no blogs here.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData()
                            }.show()
                        }
                        return@observe
                    } else {

                        val oldSize = list.size
                        stopShimmer()
                        binding.loading.visibility = View.GONE
                        binding.blogRecycler.setPadding(0,0,0,0)
                        list.clear()
                        list.addAll(it.r.topArtData)
                        Log.d("AllArtistActivity",list.size.toString()+" "+it.r.topArtData.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.topArtData.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.loading.visibility= View.GONE
                    binding.blogRecycler.setPadding(0,0,0,0)
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

        Glide.with(this@ArtistsActivity).load(R.raw.loading).into(binding.loading)
        adapter = ArtistAdapter(list)

        binding.blogRecycler.layoutManager = LinearLayoutManager(this)
        binding.blogRecycler.adapter = adapter

//        job = MainScope().launch {
//            delay(500L)
//            binding.loadinLayout.visibility = View.GONE
//
//        }
        binding.blogRecycler.addOnScrollListener(scrollListener)
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
        binding.data.visibility = View.GONE
        binding.skeletonLayout.visibility = View.VISIBLE
        binding.skeletonLayout.startShimmer()
    }

    private fun stopShimmer() {
        binding.skeletonLayout.stopShimmer()
        binding.skeletonLayout.visibility = View.GONE
        binding.data.visibility = View.VISIBLE
    }

    private fun setAppBar() {
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.app_bar)
        supportActionBar?.customView?.apply {
            findViewById<TextView>(R.id.app_bar_text).text = category
            findViewById<CircleImageView>(R.id.user_image).setImageResource(R.color.white_ex)
        }
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.white_ex))
    }

//    override fun artistClick(pos: Int) {
//        val intent = Intent(this,ProfileActivity::class.java)
//
//        var artistId = list[pos].id
//        var artistName = list[pos].name
//        var artistImg = list[pos].pictureBig
//        intent.putExtra("id",artistId)
//        intent.putExtra("img",artistImg)
//        intent.putExtra("name",artistName)
//        startActivity(intent)
//    }

}