package com.messieyawo.artistshub.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messieyawo.artistshub.databinding.AllProfileAdapterItemBinding
import com.messieyawo.artistshub.models.artistAlbums.artistAlbumData
import com.messieyawo.artistshub.profile.fragmentsAllArtist.AllArtistAlbumsProfileActivity


class AllProfileArtistAdapter(private val list: ArrayList<artistAlbumData>,
                              private val onArtistClickListener:onArtistClick,

                              ) : RecyclerView.Adapter<AllProfileArtistAdapter.VH>() {
    class VH(val binding: AllProfileAdapterItemBinding) : RecyclerView.ViewHolder(binding.root)
interface onArtistClick{
   // fun artistClick(pos: Int)
    fun viewLink(link:String)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        AllProfileAdapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val artist = list[pos]
      h.binding.rootContainerAlbumTitle.text = artist.title
        h.binding.rootReleaseDate.text = artist.releaseDate
        h.binding.rootContainerAlbumLink.text = "Lyrics : ${artist.explicitLyrics}"
        val albumLink = artist.link
         if (albumLink.isNotEmpty()){
             h.binding.rootAblbumFullTrack.text = "Type : ${artist.type } \n Fans: ${artist.fans}"
         }

        h.binding.btnPlayAlbumTracks.setOnClickListener {
                //onArtistClickListener.viewLink(albumLink)
            AllArtistAlbumsProfileActivity.start(it.context,list[pos])
       }
            //  h.binding.alb.text = artist.title + " track list"


       // h.binding.altext = artist.type

        Glide.with(h.itemView.context).load(artist.coverMedium).into(h.binding.rootArtistImageId)

        h.binding.rootArtistAlbum.setOnClickListener {
         // ProfileActivity.start(it.context,list[pos])
          //  onArtistClickListener.artistClick(pos)
         AllArtistAlbumsProfileActivity.start(it.context,list[pos])

        }
    }

    override fun getItemCount(): Int = list.size
}