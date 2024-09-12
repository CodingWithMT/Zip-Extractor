package com.example.zipfilemanger.imageFragment.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.R
import com.example.zipfilemanger.models.calculatefileSize

var tiffLongClick : ((Int) -> Unit)? = null
var tiffItemClick : ((Int) -> Unit)? = null

class TiffImgAdaptor(val context: Context, var tiffList: ArrayList<GalleryEnt>) :
    RecyclerView.Adapter<TiffImgAdaptor.ViewHolder>() {

    private var selectedTiffList: ArrayList<GalleryEnt> = ArrayList()

    private var editMode = false
    fun isEditMode(): Boolean {
        return editMode
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEditMode(mode: Boolean) {
        if (editMode != mode) {
            editMode = mode
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderImage = itemView.findViewById<ImageView>(R.id.icon_view)
        val folderName = itemView.findViewById<TextView>(R.id.file_name_text_view)
        val folderPath = itemView.findViewById<TextView>(R.id.file_path)
        var selectIcon = itemView.findViewById<ImageView>(R.id.selectItem)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.internal_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectFolder = tiffList[position]

        Glide.with(context).load(selectFolder.fPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(CenterCrop())
            .placeholder(R.drawable.image)
            .override(200, 200).into(holder.folderImage)

        val sizeFile  = calculatefileSize(selectFolder.fSize.toLong())

        holder.folderName.text = selectFolder.fName
        holder.folderPath.text = sizeFile

        holder.itemView.setOnLongClickListener {
            tiffLongClick?.invoke(position)
            true
        }
        holder.itemView.setOnClickListener {
            tiffItemClick?.invoke(position)
        }

        if (tiffList[position].isChecked) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.selectionColor))
           // holder.favourite.visibility = View.INVISIBLE
            holder.selectIcon.visibility = View.VISIBLE
            //holder.selectIcon.isChecked = result.isChecked
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
           // holder.favourite.visibility = View.VISIBLE
            holder.selectIcon.visibility = View.INVISIBLE
            // holder.checkBox.isChecked = false
        }

    }

    override fun getItemCount(): Int {
        return tiffList.size
    }

    fun getAllSelected() : ArrayList<GalleryEnt> {
        selectedTiffList = ArrayList()
        for (his in tiffList) {
            if (his.isChecked) {
                selectedTiffList.add(his)
            }
        }
        return selectedTiffList

    }

    fun setChecked(position: Int) {
        tiffList[position].isChecked = !tiffList[position].isChecked
        notifyItemChanged(position)
    }

    fun setData(list: List<GalleryEnt>) {
        tiffList = list as ArrayList<GalleryEnt>
        notifyDataSetChanged()
    }

    fun updateList(data: List<GalleryEnt>) {
        tiffList.clear()
        tiffList.addAll(data)
        notifyDataSetChanged()
    }
}