package com.messieyawo.artistshub.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.databinding.ItemGhanaArtistBinding
import com.messieyawo.artistshub.models.genreContent.GenreTypeData


class GhanaArtistAdapter(private val list: ArrayList<GenreTypeData>,
private val onItemClick:onArtistClickToOpen)
    : RecyclerView.Adapter<GhanaArtistAdapter.VH>() {
    class VH(val binding: ItemGhanaArtistBinding) : RecyclerView.ViewHolder(binding.root)
interface onArtistClickToOpen{
    fun artistClick(pos: Int)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemGhanaArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val artist = list[pos]
       h.binding.ghanaArtistName.text = artist.name
        h.binding.ghanaArtistPosition.text = "Radio :${artist.radio.toString()}"
        h.binding.ghanaArtistType.text = artist.type
//          if (artist.tracklist.isNotEmpty()){
//              h.binding.artistTracks.text = artist.name + " track list"
//          }

      //  h.binding.artistType.text = artist.type

        Glide.with(h.itemView.context).load(artist.pictureMedium).into(h.binding.ghanaArtistImage)

        h.binding.ghanaArtistRoot.setOnClickListener {
         // ProfileActivity.start(it.context,list[pos])
           onItemClick.artistClick(pos)
         // ProfileActivity.start(it.context,list[pos])

        }
    }

    override fun getItemCount(): Int = list.size
}