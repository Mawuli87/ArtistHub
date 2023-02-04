package com.messieyawo.artistshub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.databinding.ItemAllArtistTrackBinding
import com.messieyawo.artistshub.models.allArtistAlbumTracks.AllArtistAlbumTracksPlayList


class AllArtistTopTracksAdapter(private val list: ArrayList<AllArtistAlbumTracksPlayList>,
                               private val onArtistClickListener:onArtistClick,

                                ) : RecyclerView.Adapter<AllArtistTopTracksAdapter.VH>() {
    class VH(val binding: ItemAllArtistTrackBinding) : RecyclerView.ViewHolder(binding.root)

    //interfaces
interface onArtistClick{
    fun artistClick(title: String,picture:String,link:String)
    fun fullTrack(pos: String)
    fun openTrackLink(link:String)

}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemAllArtistTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val tracks = list[pos]
        h.binding.allAlbumArtistName.text = tracks.artist.name
        h.binding.allArtistAlbumTrackRank.text = tracks.title
        h.binding.allArtistAlbumTrackLink.text = tracks.type
        val previewlink = tracks.preview

        val fulltrack = tracks.link
        val title = tracks.title
        val albumCover = tracks.artist.name
          if (fulltrack.isNotEmpty()){
              h.binding.allArtistAlbumTrackRank.text = "Rank :${tracks.rank.toString()}"
          }

       // h.binding.allArtistBtnPlayTrackPreview

        Glide.with(h.itemView.context).load(R.drawable.mp3).into(h.binding.allArtistAlbumTrackImage)

      h.binding.allArtistBtnPlayTrackPreview.setOnClickListener {
         onArtistClickListener.artistClick(title,albumCover,previewlink)
      }


//        h.binding.allArtistViewAlbumTrackLoading.setOnClickListener {
//              onArtistClickListener.fullTrack(fulltrack)
//        }




        h.binding.allArtistTopTrackArtistRoot.setOnClickListener {
         // ProfileActivity.start(it.context,list[pos])

           // ProfileActivity.start(it.context,list[pos])

        }
    }

    override fun getItemCount(): Int = list.size
}