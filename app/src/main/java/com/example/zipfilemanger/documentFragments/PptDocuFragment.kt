package com.example.zipfilemanger.documentFragments

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.adaptor.DocumentAdaptor
import com.example.zipfilemanger.databinding.FragmentPptDocuBinding
import com.example.zipfilemanger.models.CompressDataClass

class PptDocuFragment : Fragment() {

    private var _binding: FragmentPptDocuBinding? = null
    private val binding get() = _binding!!

    //private var selectedGallList: ArrayList<CompressDataClass> = ArrayList()
    //private var isSelection = false

    private var pptList: MutableList<CompressDataClass> = ArrayList()
    private var pptAdaptor: DocumentAdaptor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPptDocuBinding.inflate(inflater, container, false)

        getDocuFiles()

        binding.recycleViewPptDocu.setHasFixedSize(true)
        binding.recycleViewPptDocu.layoutManager = LinearLayoutManager(requireContext())
        pptAdaptor = DocumentAdaptor(requireContext(), pptList)
        binding.recycleViewPptDocu.adapter = pptAdaptor

        return binding.root
    }
    private fun getDocuFiles(){
        val uriDocu = MediaStore.Files.getContentUri("external")
        val arrayProject = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATA
        )
        val selected = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
        val selectedArgs = arrayOf("application/vnd.ms-powerpoint")
        val sorting = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        val cursor = context?.contentResolver?.query(uriDocu, arrayProject, selected, selectedArgs, sorting)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED))
                val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                val data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                pptList.add(CompressDataClass(id,title, size,uriDocu,data))
                // Use the retrieved data as required
                Log.d("TAG", "Id: $id, NamePpt: $title, Size: $size, Date Added: $date, Mime Type: $mimeType, Data: $data")
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }
}