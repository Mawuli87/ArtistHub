package com.messieyawo.artistshub.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.databinding.ItemMainRecyclerviewBinding
import com.messieyawo.artistshub.models.genre.GenreData
import com.messieyawo.artistshub.music.GenreActivity


class GenreAdapter(
    private val list: ArrayList<GenreData>) : RecyclerView.Adapter<GenreAdapter.VH>() {
    class VH(val binding: ItemMainRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)
interface onArtistClick{
    fun artistClick(pos: Int)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemMainRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val genre = list[pos]
          val name = genre.name
        if (name == "All"){
            h.binding.recyclerviewMainText.text = "Top Artists"
            Glide.with(h.itemView.context).load(R.drawable.all).into(h.binding.recyclerviewMainImage)
        }else {
            h.binding.recyclerviewMainText.text = genre.name
            Glide.with(h.itemView.context).load(genre.pictureMedium).into(h.binding.recyclerviewMainImage)
        }


      //  h.binding.artistType.text = artist.type



        h.binding.genreRoot.setOnClickListener {
         // ProfileActivity.start(it.context,list[pos])
          //  onArtistClickListener.artistClick(pos)
        GenreActivity.start(it.context,list[pos])

        }
    }

    override fun getItemCount(): Int = list.size
}