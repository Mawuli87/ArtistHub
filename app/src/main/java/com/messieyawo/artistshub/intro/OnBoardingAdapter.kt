package com.messieyawo.artistshub.intro

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.messieyawo.artistshub.R
import com.messieyawo.artistshub.activities.MainActivity

class OnBoardingAdapter(val context: Context,
    var list:ArrayList<OnBoardingIemModel>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {


    inner class OnBoardingViewHolder(view: View): RecyclerView.ViewHolder(view){

        var image = view.findViewById<ImageView>(R.id.img)
        var title = view.findViewById<TextView>(R.id.tv_title)
       var desc = view.findViewById<TextView>(R.id.tv_desc)
        val skip = view.findViewById<TextView>(R.id.tv_skipItem)



        fun bind(model: OnBoardingIemModel){
            image.setImageResource(model.image)
            title.text = model.title
            desc.text = model.description

            skip.setOnClickListener {
                var intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(list[position])
    }
}