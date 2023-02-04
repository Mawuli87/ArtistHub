package com.messieyawo.artistshub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.activities.AllArtistProfileActivity
import com.messieyawo.artistshub.databinding.SearchArtistItemBinding
import com.messieyawo.artistshub.models.searchArtist.SearchData


class SearchArtistAdapter(private val list: ArrayList<SearchData>,
                         // private val onArtistClickListener:onArtistClick,

                          ) : RecyclerView.Adapter<SearchArtistAdapter.VH>() {
    class VH(val binding: SearchArtistItemBinding) : RecyclerView.ViewHolder(binding.root)
interface onArtistClick{
    fun artistClick(pos: Int)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        SearchArtistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val artist = list[pos]
        h.binding.searchArtistName.text = artist.name
          if (artist.tracklist.isNotEmpty()){
              h.binding.searchArtistTracks.text = artist.name + " track list"
          }

        h.binding.searchArtistType.text = artist.type

        Glide.with(h.itemView.context).load(artist.pictureMedium).into(h.binding.searchBlogImage)

        h.binding.searchArtistRoot.setOnClickListener {
         // ProfileActivity.start(it.context,list[pos])
          // onArtistClickListener.artistClick(pos)
           AllArtistProfileActivity.start(it.context,list[pos])

        }
    }

    override fun getItemCount(): Int = list.size
}