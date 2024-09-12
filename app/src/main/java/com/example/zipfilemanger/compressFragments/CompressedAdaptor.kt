package com.example.zipfilemanger.compressFragments

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.zipfilemanger.R
import com.example.zipfilemanger.models.CompressDataClass
import com.example.zipfilemanger.models.calculatefileSize

var zipLongClick : ((Int) -> Unit)? = null
var zipItemClick : ((Int) -> Unit)? = null

class CompressedAdaptor(val context: Context, var compresList: MutableList<CompressDataClass>) :
    RecyclerView.Adapter<CompressedAdaptor.ViewHolder>() {

    private var selectedGalleryList: ArrayList<CompressDataClass> = ArrayList()

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
        val view = LayoutInflater.from(context).inflate(R.layout.compress_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectFolder = compresList[position]

        val sizeFile  = calculatefileSize(selectFolder.size)

        holder.folderName.text = selectFolder.name
        holder.folderPath.text = sizeFile

        holder.itemView.setOnLongClickListener {
            zipLongClick?.invoke(position)
            true
        }
        holder.itemView.setOnClickListener {
            zipItemClick?.invoke(position)
        }

        if (compresList[position].isChecked) {
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
        return compresList.size
    }

    fun getAllSelected() : ArrayList<CompressDataClass> {
        selectedGalleryList = ArrayList()
        for (his in compresList) {
            if (his.isChecked) {
                selectedGalleryList.add(his)
            }
        }
        return selectedGalleryList

    }

    fun setChecked(position: Int) {
        compresList[position].isChecked = !compresList[position].isChecked
        notifyItemChanged(position)
    }

    fun setData(list: List<CompressDataClass>) {
        compresList = list as ArrayList<CompressDataClass>
        notifyDataSetChanged()
    }

    fun updateList(data: List<CompressDataClass>) {
        compresList.clear()
        compresList.addAll(data)
        notifyDataSetChanged()
    }
}