package com.messieyawo.artistshub.profile.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.adapters.SingleArtistAlbumAdapter
import com.messieyawo.artistshub.databinding.FragmentArtistAlbumBinding
import com.messieyawo.artistshub.events.Event
import com.messieyawo.artistshub.models.artistAlbums.artistAlbumData
import com.messieyawo.artistshub.profile.ProfileActivity
import com.messieyawo.artistshub.viewmodel.ArtistAlbumsViewModel


class ArtistAlbumFragment : Fragment(R.layout.fragment_artist_album),SingleArtistAlbumAdapter.OnItemClicked {

    lateinit var binding:FragmentArtistAlbumBinding
    private val viewmodel by viewModels<ArtistAlbumsViewModel>()
    private val list = ArrayList<artistAlbumData>()
    private var artID:String?=null
    private lateinit var adapter: SingleArtistAlbumAdapter
    var allFetched = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = true

     lateinit var fm: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentArtistAlbumBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artID = ProfileActivity.genreAllData?.id.toString()
        artID?.let { fetchData(it) }
        artID?.let { setObserver(it) }
        setRecycler()

//       adapter.setOnItemClickListener {
//           val bundle = Bundle().apply {
//               putSerializable("article", it)
//           }
//           findNavController().navigate(
//               R.id.action_artistAlbumFragment_to_artistPlayListFragment,
//               bundle
//           )
//       }

        Log.i("ARTISTID","Artist ID: ${ProfileActivity.genreAllData?.id.toString()}")


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
                        binding.singleLoadingAlbums.visibility= View.VISIBLE
                        binding.singleAlbumRecycler.setPadding(0,0,0,0)
                    }
                    isLoading=true
                }
                is Event.Success -> {
                    isLoading=false
                    if (it.r.artistAlbumData.isNullOrEmpty()) {
                        allFetched = true
                        binding.singleLoadingAlbums.visibility = View.GONE
                        if(list.isEmpty()) {
                            Snackbar.make(binding.root,"Seems there are no data here.", Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                                fetchData(artistId)
                            }.show()
                        }
                        return@observe
                    } else {
                        val oldSize = list.size
                        stopShimmer()
                       binding.singleLoadingAlbums.visibility = View.GONE
                        binding.singleAlbumRecycler.setPadding(0,0,0,0)
                        list.clear()
                        list.addAll(it.r.artistAlbumData)
                        Log.d("ArtopActivity",list.size.toString()+" "+it.r.artistAlbumData.toString())
                        adapter.notifyItemRangeInserted(oldSize,it.r.artistAlbumData.size)
                    }
                }
                is Event.Error -> {
                    isLoading=false
                    binding.singleLoadingAlbums.visibility= View.GONE
                    binding.singleAlbumRecycler.setPadding(0,0,0,0)
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

        Glide.with(requireActivity()).load(com.messieyawo.artistshub.R.raw.loading).into(binding.singleLoadingAlbums)
        adapter = SingleArtistAlbumAdapter(requireActivity(),list,this)

        binding.singleAlbumRecycler.layoutManager = LinearLayoutManager(requireActivity())
        binding.singleAlbumRecycler.adapter = adapter

//        job = MainScope().launch {
//            delay(500L)
//            binding.loadinLayout.visibility = View.GONE
//
//        }
        binding.singleAlbumRecycler.addOnScrollListener(scrollListener)
    }

//    private fun setRecycler() {
//        //       var job: Job? = null
////        binding.loadinLayout.visibility = View.VISIBLE
////        job?.cancel()
////        job = MainScope().launch {
////            delay(500L)
////                Glide.with(this@ArtistsActivity).load(R.raw.loading).into(binding.loading)
////
////        }
//
//        Glide.with(requireActivity()).load(R.raw.loading).into(binding.singleLoadingAlbums)
//        adapter = SingleArtistAlbumAdapter(requireActivity(),list)
//
//        val linearLayoutManager = LinearLayoutManager(requireActivity(),
//            LinearLayoutManager.VERTICAL ,false)
//        binding.singleAlbumRecycler.layoutManager = linearLayoutManager
//
//        binding.singleAlbumRecycler.adapter = adapter
//
////        job = MainScope().launch {
////            delay(500L)
////            binding.loadinLayout.visibility = View.GONE
////
////        }
//        binding.singleAlbumRecycler.addOnScrollListener(scrollListener)
//    }


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
                fetchData(ProfileActivity.model?.id.toString())
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
        binding.singleData.visibility = View.GONE
        binding.singleSkeletonLayoutAlbums.visibility = View.VISIBLE
        binding.singleSkeletonLayoutAlbums.startShimmer()
    }
    private fun stopShimmer() {
        binding.singleSkeletonLayoutAlbums.stopShimmer()
        binding.singleSkeletonLayoutAlbums.visibility = View.GONE
        binding.singleData.visibility = View.VISIBLE
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onStop() {
        super.onStop()
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
            R.id.action_artistAlbumFragment_to_artistPlayListFragment,args
        )



    }
}