package com.messieyawo.artistshub.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.databinding.ItemBlogBinding
import com.messieyawo.artistshub.models.artists.TopArtistsData

import com.messieyawo.artistshub.profile.ProfileActivity


class ArtistAdapter(private val list: ArrayList<TopArtistsData>,


                    ) : RecyclerView.Adapter<ArtistAdapter.VH>() {
    class VH(val binding: ItemBlogBinding) : RecyclerView.ViewHolder(binding.root)
interface onArtistClick{
    fun artistClick(pos: Int)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val artist = list[pos]
       h.binding.artistName.text = artist.name
        h.binding.artistPosition.text = artist.position.toString()
        h.binding.artistType.text = artist.type
//          if (artist.tracklist.isNotEmpty()){
//              h.binding.artistTracks.text = artist.name + " track list"
//          }

      //  h.binding.artistType.text = artist.type

        Glide.with(h.itemView.context).load(artist.pictureMedium).into(h.binding.artistImage)

        h.binding.artistRoot.setOnClickListener {
         // ProfileActivity.start(it.context,list[pos])
          //  onArtistClickListener.artistClick(pos)
          ProfileActivity.start(it.context,list[pos])

        }
    }

    override fun getItemCount(): Int = list.size
}