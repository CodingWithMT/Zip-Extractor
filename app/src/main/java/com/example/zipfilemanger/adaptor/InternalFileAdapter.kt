package com.example.zipfilemanger.adaptor


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zipfilemanger.R
import com.example.zipfilemanger.databinding.InternalItemViewBinding
import com.example.zipfilemanger.models.calculatefileSize
import java.io.File

var fileListClick : ((Int) -> Unit)? = null
class InternalFileAdapter(val context: Context, private var filesAndFolders: Array<File>) : RecyclerView.Adapter<InternalFileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = InternalItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectedFile = filesAndFolders[position]

        holder.bind(filesAndFolders[position])

        //selectedFile.length()

        holder.itemView.setOnClickListener {
            fileListClick?.invoke(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return filesAndFolders.size
    }

    inner class ViewHolder(private val binding: InternalItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(filesAndFolders: File) {
            with(binding) {
                val sizeFile  = calculatefileSize(filesAndFolders.length())
                filePath.text = sizeFile
                if (filesAndFolders.isDirectory) {
                    iconView.setImageResource(R.drawable.ic_image_folder)
                } else {
                    iconView.setImageResource(R.drawable.image)
                }
                fileNameTextView.text = filesAndFolders.name
            }
        }
    }
}