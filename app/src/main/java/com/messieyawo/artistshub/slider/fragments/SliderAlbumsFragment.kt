package com.messieyawo.artistshub.slider.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.activities.SliderDetailsActivity
import com.messieyawo.artistshub.adapters.SingleArtistSliderAlbumAdapter
import com.messieyawo.artistshub.databinding.FragmentSliderAlbumsBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.artistAlbums.artistAlbumData
import com.messieyawo.artistshub.profile.fragments.ArtistAlbumFragment
import com.messieyawo.artistshub.viewmodel.ArtistAlbumsViewModel


class SliderAlbumsFragment : Fragment(),SingleArtistSliderAlbumAdapter.OnItemClicked {
    lateinit var binding:FragmentSliderAlbumsBinding

    private val viewmodel by viewModels<ArtistAlbumsViewModel>()
    private val list = ArrayList<artistAlbumData>()
    private var artID:String?=null
    private lateinit var adapter: SingleArtistSliderAlbumAdapter
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentSliderAlbumsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artID =  SliderDetailsActivity.sliderData?.artist?.id.toString()
        artID?.let { fetchData(it) }
        artID?.let { setObserver(it) }
        setRecycler()

        Log.i("ARTISTID","Artist ID: ${ SliderDetailsActivity.sliderData?.artist?.id.toString()}")
    }

    private fun fetchData(artistId: String) {
        isLoading=true

        viewmodel.getAlbumsOfGenre(artistId)
    }

    private fun setObserver(artistId: String) {
        viewmodel.albums.observe(viewLifecycleOwner) {
            when (it) {
                is Event.Loading -> {
                    if (list.isEmpty())
                        startShimmer()
                    else {
                        binding.sliderSingleLoadingAlbums.visibility= View.VISIBLE
                        binding.sliderSingleAlbumRecycler.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false
                    if (it.r.artistAlbumData.isEmpty()) {
                        allFetched = true
                        binding.sliderSingleLoadingAlbums.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Seems there are no data here.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData(artistId)
                            }.show()
                        }
                        return@observe
                    } else {
                        val oldSize = list.size
                        stopShimmer()
                        binding.sliderSingleLoadingAlbums.visibility = View.GONE
                        binding.sliderSingleAlbumRecycler.setPadding(0,0,0,0)
                        list.clear()
                        list.addAll(it.r.artistAlbumData)
                        Log.d("ArtopActivity",list.size.toString()+" "+it.r.artistAlbumData.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.artistAlbumData.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.sliderSingleLoadingAlbums.visibility= View.GONE
                    binding.sliderSingleAlbumRecycler.setPadding(0,0,0,0)
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

        Glide.with(requireActivity()).load(com.messieyawo.artistshub.R.raw.loading).into(binding.sliderSingleLoadingAlbums)
        adapter = SingleArtistSliderAlbumAdapter(requireActivity(),list,this)

        binding.sliderSingleAlbumRecycler.layoutManager = LinearLayoutManager(requireActivity())
        binding.sliderSingleAlbumRecycler.adapter = adapter

//        job = MainScope().launch {
//            delay(500L)
//            binding.loadinLayout.visibility = View.GONE
//
//        }
        binding.sliderSingleAlbumRecycler.addOnScrollListener(scrollListener)
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


    private fun startShimmer() {
        binding.sliderSingleData.visibility = View.GONE
        binding.sliderSingleSkeletonLayoutAlbums.visibility = View.VISIBLE
        binding.sliderSingleSkeletonLayoutAlbums.startShimmer()
    }
    private fun stopShimmer() {
        binding.sliderSingleSkeletonLayoutAlbums.stopShimmer()
        binding.sliderSingleSkeletonLayoutAlbums.visibility = View.GONE
        binding.sliderSingleData.visibility = View.VISIBLE
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun getAlbumItemId(post: Int) {
        val album = list[post]
        val args = Bundle()
        args.putString("img",album.coverMedium)
        args.putString("id" ,album.id.toString())
        args.putString("title" , album.title)
        Log.i("KK",album.id.toString())
        val fragment = ArtistAlbumFragment()
        fragment.arguments = args
        findNavController().navigate(
            R.id.action_sliderAlbumsFragment_to_sliderPlayListFragment,args
        )



    }


}