package com.messieyawo.artistshub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.databinding.ItemAlbumsBinding
import com.messieyawo.artistshub.models.album.AlbumsData


class AlbumAdapter(val list: ArrayList<AlbumsData>) : RecyclerView.Adapter<AlbumAdapter.VH>() {
    class VH(val binding: ItemAlbumsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemAlbumsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val album = list[pos]
        h.binding.albumName.text = album.title
        h.binding.albumType.text = album.type
        h.binding.albumTracks.text = album.position.toString()

        Glide.with(h.itemView.context).load(album.coverMedium).into(h.binding.albumImage)

        h.binding.root.setOnClickListener {
          // BlogActivity.start(it.context,list[pos])
        }
    }

    override fun getItemCount(): Int = list.size
}