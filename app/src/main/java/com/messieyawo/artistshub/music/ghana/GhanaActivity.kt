package com.messieyawo.artistshub.music.ghana

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.adapters.GhanaArtistAdapter
import com.messieyawo.artistshub.databinding.ActivityGhanaBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.genreContent.GenreTypeData
import com.messieyawo.artistshub.music.GenreActivity
import com.messieyawo.artistshub.viewmodel.GenreTypeViewModel
import de.hdodenhof.circleimageview.CircleImageView

class GhanaActivity : AppCompatActivity(),GhanaArtistAdapter.onArtistClickToOpen {

    private val viewModel by viewModels<GenreTypeViewModel>()
    private lateinit var adapter: GhanaArtistAdapter
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true
    private val list = ArrayList<GenreTypeData>()

    lateinit var binding:ActivityGhanaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGhanaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ghanaAppBarText.text ="All Gospel Artists"
        Glide.with(this).load(R.drawable.all).into(binding.ghanaUserImage)

        // setAppBar()
        setRecycler()
        setObserver()
        fetchData()

    }
    private fun fetchData() {
        isLoading=true

        viewModel.getAllGhanaArtists()
    }

    private fun setObserver() {
        viewModel.ghana.observe(this) {
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startShimmer()
                    else {
                        binding.ghanaLoading.visibility= View.VISIBLE
                        binding.ghanaRecycler.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false

                    if (it.r.GenreTypeData.isEmpty()) {
                        allFetched = true
                        binding.ghanaLoading.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Seems there are no blogs here.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData()
                            }.show()
                        }
                        return@observe
                    } else {

                        val oldSize = list.size
                        stopShimmer()
                        binding.ghanaLoading.visibility = View.GONE
                        binding.ghanaRecycler.setPadding(0,0,0,0)
                        list.clear()
                        list.addAll(it.r.GenreTypeData)
                        Log.d("AllArtistActivity",list.size.toString()+" "+it.r.GenreTypeData.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.GenreTypeData.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.ghanaLoading.visibility= View.GONE
                    binding.ghanaRecycler.setPadding(0,0,0,0)
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

        Glide.with(this@GhanaActivity).load(R.raw.loading).into(binding.ghanaLoading)
        adapter = GhanaArtistAdapter(list,this)

        binding.ghanaRecycler.layoutManager = LinearLayoutManager(this)
        binding.ghanaRecycler.adapter = adapter

//        job = MainScope().launch {
//            delay(500L)
//            binding.loadinLayout.visibility = View.GONE
//
//        }
        binding.ghanaRecycler.addOnScrollListener(scrollListener)
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
        binding.ghanaData.visibility = View.GONE
        binding.ghanaSkeletonLayout.visibility = View.VISIBLE
        binding.ghanaSkeletonLayout.startShimmer()
    }

    private fun stopShimmer() {
        binding.ghanaSkeletonLayout.stopShimmer()
        binding.ghanaSkeletonLayout.visibility = View.GONE
        binding.ghanaData.visibility = View.VISIBLE
    }

    private fun setAppBar() {
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.customView?.apply {
            findViewById<TextView>(R.id.app_bar_text_genre).text = GenreActivity.modelGenre?.name
            findViewById<CircleImageView>(R.id.genre_user_image).setImageResource(R.color.white_ex)
        }
//        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.white_ex))
    }

    override fun artistClick(pos: Int) {
        val alertDialog = AlertDialog.Builder(this)
        val title = list[pos]
        alertDialog.apply {
            //setIcon(R.drawable.ic_hello)
            setTitle(title.name)
            setMessage("To see more about ${title.name} please go to search and search for the music title.Thank you.")
            setPositiveButton("Okay") { dialog, which ->
                //Toast.makeText(applicationContext,"continuar",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

        }.create().show()
    }


}