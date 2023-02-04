package com.messieyawo.artistshub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.databinding.ItemArtistTopTrackBinding
import com.messieyawo.artistshub.models.artistTopTracks.Data


class ArtistTopTracksAdapter(private val list: ArrayList<Data>,
                             private val onArtistClickListener:onArtistClick,

                             ) : RecyclerView.Adapter<ArtistTopTracksAdapter.VH>() {
    class VH(val binding: ItemArtistTopTrackBinding) : RecyclerView.ViewHolder(binding.root)

    //interfaces
interface onArtistClick{
    fun artistClick(title: String,picture:String,link:String)
    //fun fullTrack(pos: String)
    //fun openTrackLink(link:String)

}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemArtistTopTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val tracks = list[pos]
        h.binding.topTrackArtistName.text = tracks.title
        h.binding.topTrackArtistLink.text = "Duration :${tracks.duration.toString()}"
        val previewlink = tracks.preview

        val fulltrack = tracks.link
        val title = tracks.title
        val albumCover = tracks.album.coverMedium
          if (fulltrack.isNotEmpty()){
              h.binding.topTrackArtistRank.text = "Rank: ${tracks.rank} "
          }

        Glide.with(h.itemView.context).load(tracks.album.coverMedium).into(h.binding.topTrackBlogImage)

      h.binding.btnPlayTopTrackPreview.setOnClickListener {
          onArtistClickListener.artistClick(title,albumCover,previewlink)
      }


//        h.binding.viewTopTrackLoading.setOnClickListener {
//              onArtistClickListener.fullTrack(fulltrack)
//        }




        h.binding.topTrackArtistRoot.setOnClickListener {
         // ProfileActivity.start(it.context,list[pos])

           // ProfileActivity.start(it.context,list[pos])

        }
    }

    override fun getItemCount(): Int = list.size
}