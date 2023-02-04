package com.messieyawo.artistshub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.databinding.EachAlbumArtistTrackBinding
import com.messieyawo.artistshub.models.allArtistAlbumTracks.AllArtistAlbumTracksPlayList


class EachArtistTopTracksAdapter(private val list: ArrayList<AllArtistAlbumTracksPlayList>,
                                 private val onArtistClickListener:onArtistClick,

                                 ) : RecyclerView.Adapter<EachArtistTopTracksAdapter.VH>() {
    class VH(val binding: EachAlbumArtistTrackBinding) : RecyclerView.ViewHolder(binding.root)

    //interfaces
interface onArtistClick{
    fun artistClick(title: String,picture:String,link:String)
   // fun fullTrack(pos: String)
   // fun openTrackLink(link:String)

}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        EachAlbumArtistTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val tracks = list[pos]
        h.binding.eachAlbumArtistName.text = tracks.title
        h.binding.eachAlbumTrackRank.text = tracks.rank.toString()
      // h.binding.allArtistAlbumTrackLink.text =
        val previewlink = tracks.preview

        val fulltrack = tracks.link
        val title = tracks.title
        val albumCover = tracks.artist.name
          if (fulltrack.isNotEmpty()){
              h.binding.eachAlbumTrackLink.text = tracks.type
          }

       // h.binding.allArtistBtnPlayTrackPreview

        Glide.with(h.itemView.context).load(R.drawable.mp3).into(h.binding.eachAlbumTrackImage)

      h.binding.eachAlbumBtnPlayTrackPreview.setOnClickListener {
         onArtistClickListener.artistClick(title,albumCover,previewlink)
      }


//        h.binding. allArtistViewAlbumTrackLoading.setOnClickListener {
//              onArtistClickListener.fullTrack(fulltrack)
//        }




        h.binding.eachAlbumArtistRoot.setOnClickListener {
         // ProfileActivity.start(it.context,list[pos])

           // ProfileActivity.start(it.context,list[pos])

        }
    }

    override fun getItemCount(): Int = list.size
}