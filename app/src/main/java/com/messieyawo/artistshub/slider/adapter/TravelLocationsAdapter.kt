package com.messieyawo.artistshub.slider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.flaviofaria.kenburnsview.RandomTransitionGenerator
import com.messieyawo.artistshub.activities.SliderDetailsActivity
import com.messieyawo.artistshub.databinding.ItemContainerLocationBinding
import com.messieyawo.artistshub.models.slider.Data
import com.squareup.picasso.Picasso


class TravelLocationsAdapter(private val list: ArrayList<Data>,
private val sliderItemClick:itemSlider) :
    RecyclerView.Adapter<TravelLocationsAdapter.Vh>() {

    interface itemSlider{
        fun onItemClicked(position: Int)
    }

    inner class Vh(private val binding: ItemContainerLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(travelLocation: Data) {

            binding.apply {
                Picasso.get()
                    .load(travelLocation.album.coverBig).into(kbvLocation)

                val interpolator = AccelerateDecelerateInterpolator()

                // It is used to change the duration and
                // the interpolator of transitions

                // It is used to change the duration and
                // the interpolator of transitions
                val generator = RandomTransitionGenerator(2000, interpolator)
                kbvLocation.setTransitionGenerator(generator)

                textTitle.text = travelLocation.title
                textLocation.text = travelLocation.rank.toString()
                textStarRating.text = travelLocation.artist.name

            }
        }

        val rootLayout = binding.itemRootLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            ItemContainerLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
        holder.rootLayout.setOnClickListener {
           sliderItemClick.onItemClicked(position)
           // SliderDetailsActivity.start(it.context,list[position])
        }
    }

    override fun getItemCount(): Int = list.size
}