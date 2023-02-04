package com.messieyawo.artistshub.music

import android.content.Context
import android.content.Intent
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
import com.messieyawo.artistshub.adapters.GenreArtistAdapter
import com.messieyawo.artistshub.databinding.ActivityGenreBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.genre.GenreData
import com.messieyawo.artistshub.models.genreContent.GenreTypeData
import com.messieyawo.artistshub.profile.fragments.ArtistPlayListFragment
import com.messieyawo.artistshub.viewmodel.GenreTypeViewModel
import de.hdodenhof.circleimageview.CircleImageView

class GenreActivity : AppCompatActivity(),GenreArtistAdapter.onArtistClickToOpen {


    private val viewModel by viewModels<GenreTypeViewModel>()
    private lateinit var adapter: GenreArtistAdapter
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true
    private val list = ArrayList<GenreTypeData>()
    lateinit var binding:ActivityGenreBinding

    companion object {
        //require search result of artist to get album of a particular artist
        var modelGenre: GenreData? = null

        fun start(c: Context, ms: GenreData) {
            modelGenre = ms
            c.startActivity(Intent(c, GenreActivity::class.java))
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBarTextGenre.text = modelGenre?.name.toString()
        Glide.with(this).load(modelGenre?.pictureMedium).into(binding.genreUserImage)

       // setAppBar()
        setRecycler()
        setObserver()
        fetchData()

    }
    private fun fetchData() {
        isLoading=true

        viewModel.getListGenreType(modelGenre?.id.toString())
    }

    private fun setObserver() {
        viewModel.genreType.observe(this) {
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startShimmer()
                    else {
                        binding.genreLoading.visibility= View.VISIBLE
                        binding.genreRecycler.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false

                    if (it.r.GenreTypeData.isEmpty()) {
                        allFetched = true
                        binding.genreLoading.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Seems there are no blogs here.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData()
                            }.show()
                        }
                        return@observe
                    } else {

                        val oldSize = list.size
                        stopShimmer()
                        binding.genreLoading.visibility = View.GONE
                        binding.genreRecycler.setPadding(0,0,0,0)
                        list.clear()
                        list.addAll(it.r.GenreTypeData)
                        Log.d("AllArtistActivity",list.size.toString()+" "+it.r.GenreTypeData.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.GenreTypeData.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.genreLoading.visibility= View.GONE
                    binding.genreRecycler.setPadding(0,0,0,0)
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

        Glide.with(this@GenreActivity).load(R.raw.loading).into(binding.genreLoading)
        adapter = GenreArtistAdapter(list,this)

        binding.genreRecycler.layoutManager = LinearLayoutManager(this)
        binding.genreRecycler.adapter = adapter

//        job = MainScope().launch {
//            delay(500L)
//            binding.loadinLayout.visibility = View.GONE
//
//        }
        binding.genreRecycler.addOnScrollListener(scrollListener)
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
        binding.genreData.visibility = View.GONE
        binding.genreSkeletonLayout.visibility = View.VISIBLE
        binding.genreSkeletonLayout.startShimmer()
    }

    private fun stopShimmer() {
        binding.genreSkeletonLayout.stopShimmer()
        binding.genreSkeletonLayout.visibility = View.GONE
        binding.genreData.visibility = View.VISIBLE
    }

    private fun setAppBar() {
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.customView?.apply {
            findViewById<TextView>(R.id.app_bar_text_genre).text = modelGenre?.name
            findViewById<CircleImageView>(R.id.genre_user_image).setImageResource(R.color.white_ex)
        }
//        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.white_ex))
    }

    override fun artistClick(pos: Int) {
        val album = list[pos]
        //val intent = Intent(this@GenreActivity, ProfileActivity::class.java);
        val args = Bundle()
        args.putString("img",album.pictureMedium)
        args.putString("id" ,album.id.toString())
        args.putString("title" , album.name)
        val fragment = ArtistPlayListFragment()
        fragment.arguments = args
       // startActivity(intent)

        Log.i("KK",album.id.toString())

    }
}