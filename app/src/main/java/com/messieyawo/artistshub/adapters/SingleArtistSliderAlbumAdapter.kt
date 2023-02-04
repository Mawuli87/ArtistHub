package com.messieyawo.artistshub.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.databinding.ItemArtistSliderAlbumTrackBinding
import com.messieyawo.artistshub.models.artistAlbums.artistAlbumData


class SingleArtistSliderAlbumAdapter(context: Context,
                                     val list: ArrayList<artistAlbumData>,
                                     private val onAlbumItemClickedListener:OnItemClicked
) :
    RecyclerView.Adapter<SingleArtistSliderAlbumAdapter.VH>() {
    class VH(val binding: ItemArtistSliderAlbumTrackBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemArtistSliderAlbumTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    interface OnItemClicked{
        fun getAlbumItemId(post:Int)
    }

    private var onItemClickListener: ((artistAlbumData) -> Unit)? = null

    override fun onBindViewHolder(h: VH, pos: Int) {
        val album = list[pos]
        h.binding.albumTrackArtistTitle.text = album.title
        h.binding.albumTrackArtistLink.text = album.link
        h.binding.albumTrackArtistReleaseDate.text = "Released: ${album.releaseDate}"
        h.binding.albumTrackArtistFullType.text = album.type

         if ( h.binding.albumTrackArtistLink.text .isNotEmpty()){
             h.binding.albumTrackArtistLink.text = "Fans: ${album.fans} "
         }


        Glide.with(h.itemView.context).load(album.coverMedium).into(h.binding.albumArtistTrackImage)

//        h.binding.root.setOnClickListener {
//           ProfileActivity.start(it.context,list[pos])
//
//        }
        h.binding.btnViewAlbumTracks.setOnClickListener {
          onAlbumItemClickedListener.getAlbumItemId(pos)
          // ProfileActivity.start(it.context,list[pos])
        }
//        h.itemView.apply {
//            setOnClickListener {
//                onItemClickListener?.let { it(pos) }
//            }
//        }


    }

    override fun getItemCount(): Int = list.size

//    fun setOnItemClickListener(listener: (artistAlbumData) -> Unit) {
//        onItemClickListener = listener
//    }
}