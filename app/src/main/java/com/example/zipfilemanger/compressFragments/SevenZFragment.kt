package com.example.zipfilemanger.compressFragments

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.databinding.FragmentSevenZBinding
import com.example.zipfilemanger.models.CompressDataClass
import java.io.File

class SevenZFragment : Fragment() {
    private var _binding: FragmentSevenZBinding? = null
    private val binding get() = _binding!!

    //private var selectedGallList: ArrayList<CompressDataClass> = ArrayList()
    //private var isSelection = false
    //private latent var viewVModelGCompress: ViewModelGallery

    private var list7z: MutableList<CompressDataClass> = ArrayList()
    private var adaptor7z: Compressed7zAdaptor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSevenZBinding.inflate(inflater, container, false)

        getSevenZFiles()

        binding.recycleViewSevenZ.setHasFixedSize(true)
        binding.recycleViewSevenZ.layoutManager = LinearLayoutManager(requireContext())

        val files = ArrayList<CompressDataClass>()
        val uriCompress = MediaStore.Files.getContentUri("external")
        val directory = File(Environment.getExternalStorageDirectory(), "7z_files")
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()!!.forEach {
                if (it.isFile && it.extension == "7z") {
                    files.add(CompressDataClass(1, it.name,21,uriCompress, it.path))
                }
            }
        }

        binding.recycleViewSevenZ.setHasFixedSize(true)
        binding.recycleViewSevenZ.layoutManager = LinearLayoutManager(requireContext())
        adaptor7z = Compressed7zAdaptor(requireContext(),list7z)
        binding.recycleViewSevenZ.adapter = adaptor7z

        if (list7z.isEmpty()) {
            binding.no7zLayout.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun getSevenZFiles() {
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
        val selectedArgs = arrayOf("application/x-7z-compressed")
        //val selectedArgs = MimeTypeMap.getSingleton().getMimeTypeFromExtension("7z")
        val sorting = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        val cursor = context?.contentResolver?.query(uriCompress, arrayProject, selected, selectedArgs, sorting)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED))
                val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                val data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                list7z.add(CompressDataClass(id, title, size,uriCompress, data))
                // Use the retrieved data as required
                Log.d("TAG", "Id: $id, Name: $title, Size: $size, Date Added: $date, Mime Type: $mimeType, Data: $data")
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }

    fun getSevenZipFiles(): List<CompressDataClass> {
        val uriCompress = MediaStore.Files.getContentUri("external")
        val files = mutableListOf<CompressDataClass>()
        val rootDirectory = context?.filesDir

        rootDirectory?.listFiles()?.forEach { file ->
            if (file.extension == "7z") {
                files.add(CompressDataClass(12,file.nameWithoutExtension,12,uriCompress, file.absolutePath))
            }
        }
        return files
    }
}