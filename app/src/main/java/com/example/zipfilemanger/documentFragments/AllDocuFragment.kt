package com.example.zipfilemanger.documentFragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.R
import com.example.zipfilemanger.adaptor.DocumentAdaptor
import com.example.zipfilemanger.adaptor.documentItemClick
import com.example.zipfilemanger.adaptor.documentLongClick
import com.example.zipfilemanger.audioFragment.adaptor.audioItemClick
import com.example.zipfilemanger.audioFragment.adaptor.audioLongClick
import com.example.zipfilemanger.databinding.FragmentAllDocuBinding
import com.example.zipfilemanger.models.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


class AllDocuFragment : Fragment() {


    private var _binding: FragmentAllDocuBinding? = null
    private val binding get() = _binding!!

    private var selectedDocuList: ArrayList<CompressDataClass> = ArrayList()
    private var isSelection = false

    var fileName = ""
    private lateinit var bottomSheetBehaviorDocu: BottomSheetBehavior<LinearLayout>
    private lateinit var compressLayout : ConstraintLayout
    private lateinit var textCount : TextView
    private lateinit var compressBtn : ConstraintLayout

    private var listDocument: MutableList<CompressDataClass> = ArrayList()
    private var adaptorDocument: DocumentAdaptor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllDocuBinding.inflate(inflater, container, false)

        compressLayout = requireActivity().findViewById(R.id.bottom_layout_Docu)
        compressBtn = requireActivity().findViewById(R.id.compressed_layout_Docu)
        textCount = requireActivity().findViewById(R.id.text_Count_Docu)

        getDocuFiles()

        binding.recycleViewAllDocu.setHasFixedSize(true)
        binding.recycleViewAllDocu.setItemViewCacheSize(13)
        binding.recycleViewAllDocu.layoutManager = LinearLayoutManager(requireContext())
        adaptorDocument = DocumentAdaptor(requireContext(), listDocument)
        binding.recycleViewAllDocu.adapter = adaptorDocument

        documentLongClick = {
            adaptorDocument?.setEditMode(true)
            adaptorDocument?.setChecked(it)
            isSelection = true

            selectedDocuList = adaptorDocument!!.getAllSelected()
            textCount.text = "Items ${selectedDocuList.size}"

            compressLayout.visibility = View.VISIBLE

            // textCount.text = CompressDataClass.allImageList.size.toString()
//           compressImage.visibility = View.VISIBLE
            //compressLayout.visibility = View.VISIBLE
        }

        documentItemClick = {
            //getPosition = it
            if (isSelection){
                adaptorDocument?.setChecked(it)

                selectedDocuList = adaptorDocument!!.getAllSelected()
                textCount.text = "Items ${selectedDocuList.size}"

                //compressLayout.visibility = View.VISIBLE
            }
        }

        btnOkBottomSheetDocu = {
            val includeBottom = requireActivity().findViewById<LinearLayout>(R.id.include_Bottom_Sheet_Docu)
            fileName = includeBottom.filenameInput.text.toString()
            zipList(fileName)

            for (n in listDocument.indices) {
                listDocument[n].isChecked = false
            }
            isSelection = false
            adaptorDocument?.setEditMode(false)
            adaptorDocument?.notifyDataSetChanged()
        }

        cancelBottomSheetDocu = {
            if (isSelection){
                for (n in listDocument.indices) {
                    listDocument[n].isChecked = false
                }
                isSelection = false
                adaptorDocument?.setEditMode(false)
            }
            adaptorDocument?.notifyDataSetChanged()
        }

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
        val selectedArgs = arrayOf("application/pdf")
        //,"application/docx","application/pps","application/txt","application/xml"
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
                listDocument.add(CompressDataClass(id,title, size,uriDocu,data))
                // Use the retrieved data as required
                Log.d("TAG", "Id: $id, Name: $title, Size: $size, Date Added: $date, Mime Type: $mimeType, Data: $data")
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }

    private fun zipList(name : String){
        val zipFiles = adaptorDocument?.getAllSelected()
        val fileList: MutableList<File> = mutableListOf()
        for (i in zipFiles!!) {
            val f = File(i.path)
            if (f.exists()) {
                fileList.add(f)
            }
        }
        val filePath = File("/storage/emulated/0/$name.zip")

        //val filePath = File(imagePath)
        zipFiles(fileList, filePath)
        Toast.makeText(requireContext(), "This is path : $filePath", Toast.LENGTH_SHORT).show()
    }
    private fun zipFiles(files: List<File>, targetFile: File) {
        val buffer = ByteArray(1024)
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            val fos = FileOutputStream(targetFile)
            val bos = BufferedOutputStream(fos)
            val zos = ZipOutputStream(bos)
            // loop through each file and add it to the zip file
            for (file in files) {
                val fis = FileInputStream(file)
                val bis = BufferedInputStream(fis)
                val entry = ZipEntry(file.name)
                zos.putNextEntry(entry)
                // write the file contents to the zip file
                var len: Int
                while (bis.read(buffer).also { len = it } > 0) {
                    zos.write(buffer, 0, len)
                }
                bis.close()
                zos.closeEntry()
            }
            zos.close()
        }
    }
}