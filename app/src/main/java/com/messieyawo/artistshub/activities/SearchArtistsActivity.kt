package com.messieyawo.artistshub.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.adapters.SearchArtistAdapter
import com.messieyawo.artistshub.databinding.ActivitySearchArtistsBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.searchArtist.SearchData

import com.messieyawo.artistshub.viewmodel.SearchArtistViewModel
import java.util.*

class SearchArtistsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchArtistsBinding

    private val viewmodelSearch by viewModels<SearchArtistViewModel>()
   private lateinit var adapter: SearchArtistAdapter
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true
    private val list = ArrayList<SearchData>()


    companion object {
        var category = ""


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchArtistsBinding.inflate(layoutInflater)
        setContentView(binding.root)

         observe()
        setRecycler()

        binding.searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")){
                    Snackbar.make(binding.root,"Please enter search query.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        binding.searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
                            override fun onQueryTextSubmit(query: String?): Boolean {

                                return true
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                if (newText.equals("")){
                                    Snackbar.make(binding.root,"Please enter search query.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                        var str2=  binding.searchView.query.toString()
                                        var str = str2
                                        str = str.split(":").joinToString("")
                                        str = str.split(" ").joinToString("-").lowercase(Locale.ROOT)
                                        viewmodelSearch.getAllSearchArtists(str)
                                        observe()
                                        setRecycler()
                                    }.show()
                                }else {
                                    var str = newText
                                    str = str!!.split(":").joinToString("")
                                    str = str!!.split(" ").joinToString("-").lowercase(Locale.ROOT)
                                    viewmodelSearch.getAllSearchArtists(str!!)
                                }
                                return true
                            }

                        })
                    }.show()
                }else {
                    if (newText.equals("")){
                        Snackbar.make(binding.root,"Please enter search query.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                            var str1=  binding.searchView.query.toString()
                            var str = str1
                            str = str.split(":").joinToString("")
                            str = str.split(" ").joinToString("-").lowercase(Locale.ROOT)
                            viewmodelSearch.getAllSearchArtists(str)
                        }.show()
                    }else {
                        var str = newText
                        str = str!!.split(":").joinToString("")
                        str = str!!.split(" ").joinToString("-").lowercase(Locale.ROOT)
                        viewmodelSearch.getAllSearchArtists(str!!)
                        observe()
                        setRecycler()
                    }
                }
                return true
            }

        })



    }



    private fun observe() {
        viewmodelSearch.searchArtist.observe(this){
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startShimmer()
                    else {
                        binding.searchLoading.visibility= View.VISIBLE
                        binding.searchBlogRecycler.setPadding(0,0,0,30)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false
                    if (it.r.searchData.isEmpty()) {
                        allFetched = true
                        binding.searchLoading.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Seems there are no data here.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {

                            }.show()
                        }
                        return@observe
                    } else {
                        val oldSize = 0
                        stopShimmer()
                        binding.searchLoading.visibility = View.GONE
                        binding.searchBlogRecycler.setPadding(0,0,0,0)
                        list.clear()
                        list.addAll(it.r.searchData)
                        Log.d("CategoryActivity",list.size.toString()+" "+it.r.searchData.size.toString())
                        adapter.notifyItemRangeInserted(it.r.searchData.size,oldSize)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.searchLoading.visibility= View.GONE
                    binding.searchBlogRecycler.setPadding(0,0,0,0)
                    Snackbar.make(binding.root,it.msg, Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        binding.searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
                            override fun onQueryTextSubmit(query: String?): Boolean {

                                return true
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                viewmodelSearch.getAllSearchArtists(newText.toString())
                                return true
                            }

                        })
                    }.show()
                }
                else -> {}
            }
        }
        }

    private fun startShimmer() {
        binding.saerchData.visibility = View.GONE
        binding.skeletonLayout.visibility = View.VISIBLE
        binding.skeletonLayout.startShimmer()
    }

    private fun stopShimmer() {
        binding.skeletonLayout.stopShimmer()
        binding.skeletonLayout.visibility = View.GONE
        binding.saerchData.visibility = View.VISIBLE
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

        Glide.with(this@SearchArtistsActivity).load(R.raw.loading).into(binding.searchLoading)
        adapter = SearchArtistAdapter(list)

        binding.searchBlogRecycler.layoutManager = LinearLayoutManager(this)
        binding.searchBlogRecycler.adapter = adapter

//        job = MainScope().launch {
//            delay(500L)
//            binding.loadinLayout.visibility = View.GONE
//
//        }
       // binding.searchBlogRecycler.addOnScrollListener(scrollListener)
    }

//    override fun artistClick(pos: Int) {
//        val intent = Intent(this, AllArtistProfileActivity::class.java)
//
//        val artistId = list[pos].id
//        val artistName = list[pos].name
//        val artistImg = list[pos].pictureBig
//        intent.putExtra("id",artistId)
//        intent.putExtra("img",artistImg)
//        intent.putExtra("name",artistName)
//        startActivity(intent)
//    }


    }


