package com.example.zipfilemanger.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.R

class AudioAdaptor(val context: Context, var audioList: ArrayList<GalleryEnt>) :
    RecyclerView.Adapter<AudioAdaptor.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderImage = itemView.findViewById<ImageView>(R.id.icon_view)
        val folderName = itemView.findViewById<TextView>(R.id.file_name_text_view)
        val folderPath = itemView.findViewById<TextView>(R.id.file_path)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.audio_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectFolder = audioList[position]

        Glide.with(context).load(selectFolder.fPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(CenterCrop())
            .override(200, 200).into(holder.folderImage)

        Glide.with(context)
            .load(selectFolder.fPath)
            .placeholder(R.drawable.mp3)
            .into(holder.folderImage)

        holder.folderName.text = selectFolder.fName
        holder.folderPath.text = selectFolder.fPath
    }

    override fun getItemCount(): Int {
        return audioList.size
    }

    fun setData(list: List<GalleryEnt>) {
        audioList = list as ArrayList<GalleryEnt>
        notifyDataSetChanged()
    }

    fun updateList(data: List<GalleryEnt>) {
        audioList.clear()
        audioList.addAll(data)
        notifyDataSetChanged()
    }
}