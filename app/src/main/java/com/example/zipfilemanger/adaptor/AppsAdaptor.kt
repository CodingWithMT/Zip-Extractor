package com.example.zipfilemanger.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.R
import com.example.zipfilemanger.models.CompressDataClass
import com.example.zipfilemanger.models.calculatefileSize

var appsLongClick : ((Int) -> Unit)? = null
var appsItemClick : ((Int) -> Unit)? = null

class AppsAdaptor(val context: Context, var listApps: ArrayList<CompressDataClass>) :
    RecyclerView.Adapter<AppsAdaptor.ViewHolder>() {

    private var selectedAppsList: ArrayList<CompressDataClass> = ArrayList()

    private var editMode = false
    fun setEditMode(mode: Boolean) {
        if (editMode != mode) {
            editMode = mode
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderImage = itemView.findViewById<ImageView>(R.id.icon_view)!!
        val folderName = itemView.findViewById<TextView>(R.id.file_name_text_view)!!
        val folderPath = itemView.findViewById<TextView>(R.id.file_path)!!
        var selectIcon = itemView.findViewById<ImageView>(R.id.selectItem)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.apks_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectFolder = listApps[position]

 /*       Glide.with(context).load(selectFolder.fPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(CenterCrop())
            .override(200, 200).into(holder.folderImage)*/

        Glide.with(context)
            .load(selectFolder.path)
            .placeholder(R.drawable.apk_files)
            .into(holder.folderImage)

        val sizeFile  = calculatefileSize(selectFolder.size)

        holder.folderName.text = selectFolder.name
        holder.folderPath.text = sizeFile

        holder.itemView.setOnLongClickListener {
            appsLongClick?.invoke(position)
            true
        }
        holder.itemView.setOnClickListener {
            appsItemClick?.invoke(position)
        }

        if (listApps[position].isChecked) {
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
        return listApps.size
    }

    fun getAllSelected() : ArrayList<CompressDataClass> {
        selectedAppsList = ArrayList()
        for (his in listApps) {
            if (his.isChecked) {
                selectedAppsList.add(his)
            }
        }
        return selectedAppsList
    }

    fun setChecked(position: Int) {
        listApps[position].isChecked = !listApps[position].isChecked
        notifyItemChanged(position)
    }

    fun setData(list: List<GalleryEnt>) {
        listApps = list as ArrayList<CompressDataClass>
        notifyDataSetChanged()
    }

    fun updateList(data: List<CompressDataClass>) {
        listApps.clear()
        listApps.addAll(data)
        notifyDataSetChanged()
    }
}