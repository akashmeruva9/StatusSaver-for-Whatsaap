package com.example.socialmediadownloadeapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.ArrayList

class statusadapter(private val listener: BlankFragment, private var data: ArrayList<statusdata>)
    : RecyclerView.Adapter<statusviewholder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): statusviewholder {

        val view =  LayoutInflater.from(parent.context).inflate(R.layout.whatsappitem, parent , false)
        val ststview = statusviewholder(view)

        view.setOnClickListener {
            listener.statusclicked(data[ststview.adapterPosition])
        }

        return ststview
    }

    override fun onBindViewHolder(holder: statusviewholder, position: Int) {
        if (data[position].uri.endsWith(".mp4")){

            holder.play.visibility = View.VISIBLE

        }else {

            holder.play.visibility = View.GONE
        }

        Glide.with(holder.itemView.context).load(Uri.parse(data[position].uri)).into(holder.image)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
class statusviewholder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val image :ImageView = itemView.findViewById(R.id.whatsapp_image)
    val play :ImageView = itemView.findViewById(R.id.playbutton)

}

interface status {

    fun statusclicked(item : statusdata)
}

