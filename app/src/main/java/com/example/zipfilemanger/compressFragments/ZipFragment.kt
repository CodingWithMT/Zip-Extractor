package com.example.zipfilemanger.compressFragments

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.R
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.databinding.FragmentZipBinding
import com.example.zipfilemanger.models.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class ZipFragment : Fragment() {
    private var _binding: FragmentZipBinding? = null
    private val binding get() = _binding!!

    //private var selectedGallList: ArrayList<CompressDataClass> = ArrayList()
    var isSelection = false
    //private latent var viewVModelGCompress: ViewModelGallery

    private var zipList: MutableList<CompressDataClass> = ArrayList()
    private var selectedZipList: ArrayList<CompressDataClass> = ArrayList()
    private var zipAdaptor: CompressedAdaptor? = null


    private var fileName = ""
    //private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var deCompressLayout : ConstraintLayout
    private lateinit var textCount : TextView
    // private  var includeBottomSheet : CoordinatorLayout
    private lateinit var bGBottomSheet : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentZipBinding.inflate(inflater, container, false)

        deCompressLayout = requireActivity().findViewById(R.id.bottom_layout_Zip)
        bGBottomSheet = requireActivity().findViewById(R.id.bottomSheetBGZip)
        textCount = requireActivity().findViewById(R.id.text_Count_Zip)

        zipAdaptor?.setData(zipList)
        zipList.clear()

        getZipFiles()

        binding.recycleViewZip.setHasFixedSize(true)
        binding.recycleViewZip.layoutManager = LinearLayoutManager(requireContext())
        zipAdaptor = CompressedAdaptor(requireContext(), zipList)
        binding.recycleViewZip.adapter = zipAdaptor

        if (zipList.isEmpty()) {
            binding.noZipLayout.visibility = View.VISIBLE
        }

        zipLongClick = {
            zipAdaptor?.setEditMode(true)
            isSelection = true
            zipAdaptor?.setChecked(it)

            if (zipList.size <= 1){
                zipAdaptor?.setEditMode(false)
                isSelection = false
                zipAdaptor?.setChecked(it)
            }
            selectedZipList = zipAdaptor!!.getAllSelected()
            textCount.text = "Items ${selectedZipList.size}"

           deCompressLayout.visibility = View.VISIBLE
        }

        zipItemClick = {
            if (isSelection){
                if (zipList.size <= 1){
                    zipAdaptor?.setChecked(it)
                }
                selectedZipList = zipAdaptor!!.getAllSelected()
                textCount.text = "Items ${selectedZipList.size}"
            }
        }

        btnOkBottomSheetZip = {
            val includeBottom = requireActivity().findViewById<LinearLayout>(R.id.include_Bottom_SheetZip)
            fileName = includeBottom.filenameInput.text.toString()
            //zipList(fileName)


            selectedZipList = zipAdaptor!!.getAllSelected()
            for (i in selectedZipList) {
                unzipFil(i.path, "/sdcard/$fileName")
            }

            for (n in zipList.indices) {
                zipList[n].isChecked = false
            }
            isSelection = false
            zipAdaptor?.setEditMode(false)
            zipAdaptor?.notifyDataSetChanged()
        }

        cancelBottomSheetZip = {
            if (isSelection){
                for (n in zipList.indices) {
                    zipList[n].isChecked = false
                }
                isSelection = false
                zipAdaptor?.setEditMode(false)
            }
            zipAdaptor?.notifyDataSetChanged()
        }
        return binding.root
    }
    private fun unzipFil(zipFilePath: String, destinationFolder: String) {
        val bufferSize = 4096
        val buffer = ByteArray(bufferSize)

        // Create a ZipInputStream to read the contents of the zip file
        val zipInputStream = ZipInputStream(BufferedInputStream(FileInputStream(zipFilePath)))

        // Iterate over each entry in the zip file and extract it to the destination folder
        var zipEntry: ZipEntry? = zipInputStream.nextEntry
        while (zipEntry != null) {
            // Get the name of the entry
            val entryName = zipEntry.name

            // Create a new file in the destination folder with the same name as the entry
            val outputFile = File(destinationFolder, entryName)

            // If the entry is a directory, create it in the destination folder
            if (zipEntry.isDirectory) {
                outputFile.mkdirs()
            } else {
                // If the entry is a file, extract it to the destination folder
                val parent = File(outputFile.parent)
                if (!parent.exists()) {
                    parent.mkdirs()
                }
                val fileOutputStream = FileOutputStream(outputFile)
                var count: Int
                while (zipInputStream.read(buffer, 0, bufferSize).also { count = it } != -1) {
                    fileOutputStream.write(buffer, 0, count)
                }
                fileOutputStream.close()
            }
            zipEntry = zipInputStream.nextEntry
        }
        // Close the ZipInputStream
        zipInputStream.close()
    }

    private fun getZipFiles(){
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
        val selectedArgs = arrayOf("application/zip")
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
                zipList.add(CompressDataClass(id,title, size,uriCompress,data))
                // Use the retrieved data as required
                Log.d("TAG", "Id: $id, Name: $title, Size: $size, Date Added: $date, Mime Type: $mimeType, Data: $data")
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }
}