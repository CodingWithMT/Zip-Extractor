package com.example.zipfilemanger.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.R
import com.example.zipfilemanger.compressFragments.zipItemClick
import com.example.zipfilemanger.compressFragments.zipLongClick
import com.example.zipfilemanger.models.CompressDataClass
import com.example.zipfilemanger.models.calculatefileSize

var documentLongClick : ((Int) -> Unit)? = null
var documentItemClick : ((Int) -> Unit)? = null

class DocumentAdaptor(val context: Context, var docuList: MutableList<CompressDataClass>) :
    RecyclerView.Adapter<DocumentAdaptor.ViewHolder>() {

    private var selectedDocuList: ArrayList<CompressDataClass> = ArrayList()

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
        val view = LayoutInflater.from(context).inflate(R.layout.document_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectFolder = docuList[position]

       /* Glide.with(context).load(selectFolder.fPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(CenterCrop())
            .override(200, 200).into(holder.folderImage)*/

        val sizeFile  = calculatefileSize(selectFolder.size)

        holder.folderName.text = selectFolder.name
        holder.folderPath.text = sizeFile

        holder.itemView.setOnLongClickListener {
           documentLongClick?.invoke(position)
            true
        }
        holder.itemView.setOnClickListener {
         documentItemClick?.invoke(position)
        }

        if (docuList[position].isChecked) {
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
        return docuList.size
    }

    fun getAllSelected() : ArrayList<CompressDataClass> {
        selectedDocuList = ArrayList()
        for (his in docuList) {
            if (his.isChecked) {
                selectedDocuList.add(his)
            }
        }
        return selectedDocuList
    }

    fun setChecked(position: Int) {
        docuList[position].isChecked = !docuList[position].isChecked
        notifyItemChanged(position)
    }

    fun setData(list: List<GalleryEnt>) {
        docuList = list as ArrayList<CompressDataClass>
        notifyDataSetChanged()
    }

    fun updateList(data: List<CompressDataClass>) {
        docuList.clear()
        docuList.addAll(data)
        notifyDataSetChanged()
    }
}