package com.example.zipfilemanger.compressFragments

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.databinding.FragmentRarBinding
import com.example.zipfilemanger.models.CompressDataClass


class RarFragment : Fragment() {
    private var _binding: FragmentRarBinding? = null
    private val binding get() = _binding!!

    private var selectedGallList: ArrayList<CompressDataClass> = ArrayList()
    private var isSelection = false
    //private latent var viewVModelGCompress: ViewModelGallery

    private var rarList: MutableList<CompressDataClass> = ArrayList()
    private var rarAdaptor: CompressedAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRarBinding.inflate(inflater, container, false)

        getRarFiles()

        binding.recycleViewRar.setHasFixedSize(true)
        binding.recycleViewRar.layoutManager = LinearLayoutManager(requireContext())
        rarAdaptor = CompressedAdaptor(requireContext(), rarList)
        binding.recycleViewRar.adapter = rarAdaptor

        if (rarList.isEmpty()) {
            binding.noRarLayout.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun getRarFiles(){
        val uriCompress = MediaStore.Files.getContentUri("external")
        val arrayProject = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATA
        )
        val selected = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
        val selectedArgs = arrayOf("application/rar")
        val sorting = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        val cursor = context?.contentResolver?.query(
            uriCompress, arrayProject, selected, selectedArgs, sorting)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED))
                val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                val data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                rarList.add(CompressDataClass(id,title, size,uriCompress,data))
                // Use the retrieved data as required
                Log.d("TAG", "Id: $id, Name: $title, Size: $size, Date Added: $date, Mime Type: $mimeType, Data: $data")
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }
}