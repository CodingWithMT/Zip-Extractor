package com.example.zipfilemanger.videosFragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.R
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.dataBase.ViewModelGallery
import com.example.zipfilemanger.databinding.FragmentAllVidBinding
import com.example.zipfilemanger.models.*
import com.example.zipfilemanger.videosFragment.adaptor.AllVidAdaptor
import com.example.zipfilemanger.videosFragment.adaptor.allVidItemClick
import com.example.zipfilemanger.videosFragment.adaptor.allVidLongClick
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class AllVidFragment : Fragment() {

    private var _binding: FragmentAllVidBinding? = null
    private val binding get() = _binding!!

        private var isSelection = false
        var fileName = ""
        private lateinit var bottomSheetBehaviorVid: BottomSheetBehavior<LinearLayout>
        private lateinit var compressLayout : ConstraintLayout
        private lateinit var textCount : TextView
        private lateinit var compressBtn : ConstraintLayout

    private var selectedVidList: ArrayList<GalleryEnt> = ArrayList()
    //private var isSelection = false
    private lateinit var viewVModelG: ViewModelGallery

    private var listVidAll: ArrayList<GalleryEnt> = ArrayList()
    private var adaptorVideos: AllVidAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllVidBinding.inflate(inflater, container, false)

        compressLayout = requireActivity().findViewById(R.id.bottom_layout_Vid)
        compressBtn = requireActivity().findViewById(R.id.compressed_layout_Vid)
        textCount = requireActivity().findViewById(R.id.text_Count_Vid)

        viewVModelG = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelG.getAllVideos().observe(requireActivity()) {
            adaptorVideos?.setData(it)
        }

        binding.recycleViewVideoAll.setHasFixedSize(true)
        binding.recycleViewVideoAll.layoutManager = LinearLayoutManager(requireContext())
        adaptorVideos = AllVidAdaptor(requireContext(), listVidAll)
        binding.recycleViewVideoAll.adapter = adaptorVideos

        allVidLongClick = {
            adaptorVideos?.setEditMode(true)
            adaptorVideos?.setChecked(it)
            isSelection = true

            selectedVidList = adaptorVideos!!.getAllSelected()
            textCount.text = "Items ${selectedVidList.size}"

            compressLayout.visibility = View.VISIBLE

            textCount.text = CompressDataClass.allImageList.size.toString()
//           compressImage.visibility = View.VISIBLE
            compressLayout.visibility = View.VISIBLE
        }

        allVidItemClick = {
            //getPosition = it
            if (isSelection){
                adaptorVideos?.setChecked(it)

                selectedVidList = adaptorVideos!!.getAllSelected()
                textCount.text = "Items ${selectedVidList.size}"

                compressLayout.visibility = View.VISIBLE
            }
        }

        btnOkBottomSheetVid = {
            val includeBottom = requireActivity().findViewById<LinearLayout>(R.id.include_Bottom_Sheet_Vid)
            fileName = includeBottom.filenameInput.text.toString()
            zipList(fileName)

            for (n in listVidAll.indices) {
                listVidAll[n].isChecked = false
            }
            isSelection = false
            adaptorVideos?.setEditMode(false)
            adaptorVideos?.notifyDataSetChanged()
        }

        cancelBottomSheetVid = {
            if (isSelection){
                for (n in listVidAll.indices) {
                    listVidAll[n].isChecked = false
                }
                isSelection = false
                adaptorVideos?.setEditMode(false)
            }
            adaptorVideos?.notifyDataSetChanged()
        }

        return binding.root
    }

    private fun zipList(name : String){
        val zipFiles = adaptorVideos?.getAllSelected()
        val fileList: MutableList<File> = mutableListOf()
        for (i in zipFiles!!) {
            val f = File(i.fPath)
            if (f.exists()) {
                fileList.add(f)
            }
        }
        /* if (fileList.isNotEmpty()) {
             fileList.toTypedArray()
         }
               if (!filePath.exists()){
             filePath.mkdir()
         }*/
        val filePath = File("/storage/emulated/0/$name.zip")

        //val filePath = File(imagePath)
        zipFiles(fileList, filePath)
        Toast.makeText(requireContext(), "This is path : $filePath", Toast.LENGTH_SHORT).show()
        //val filePath = File(imagePath)
        //zipFiles(fileList, filePath)
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