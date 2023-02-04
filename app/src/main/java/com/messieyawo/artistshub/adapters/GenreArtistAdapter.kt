package com.messieyawo.artistshub.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.databinding.ItemGenreArtistBinding
import com.messieyawo.artistshub.models.genreContent.GenreTypeData
import com.messieyawo.artistshub.profile.ProfileActivity


class GenreArtistAdapter(private val list: ArrayList<GenreTypeData>,
                         private val ItemClicked:onArtistClickToOpen)
    : RecyclerView.Adapter<GenreArtistAdapter.VH>() {
    class VH(val binding: ItemGenreArtistBinding) : RecyclerView.ViewHolder(binding.root)
interface onArtistClickToOpen{
    fun artistClick(pos: Int)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemGenreArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val artist = list[pos]
       h.binding.genreArtistName.text = artist.name
        h.binding.genreArtistPosition.text = "Radio :${artist.radio.toString()}"
        h.binding.genreArtistType.text = artist.type
//          if (artist.tracklist.isNotEmpty()){
//              h.binding.artistTracks.text = artist.name + " track list"
//          }

      //  h.binding.artistType.text = artist.type

        Glide.with(h.itemView.context).load(artist.pictureMedium).into(h.binding.genreArtistImage)

        h.binding.genreArtistRoot.setOnClickListener {
         // ProfileActivity.start(it.context,list[pos])
           // ItemClicked.artistClick(pos)
          ProfileActivity.start(it.context,list[pos])

        }
    }

    override fun getItemCount(): Int = list.size
}