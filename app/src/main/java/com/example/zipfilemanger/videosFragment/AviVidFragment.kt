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
import com.example.zipfilemanger.databinding.FragmentAviVidBinding
import com.example.zipfilemanger.imageFragment.JpgImgFragment
import com.example.zipfilemanger.models.CompressDataClass
import com.example.zipfilemanger.models.hideKeyBoard
import com.example.zipfilemanger.videosFragment.adaptor.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class AviVidFragment : Fragment() {
    private var _binding: FragmentAviVidBinding? = null
    private val binding get() = _binding!!

    private var isSelection = false
    var fileName = ""
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var compressLayout : ConstraintLayout
    private lateinit var textCount : TextView
    private lateinit var compressBtn : ConstraintLayout

    //private var selectedGallList: ArrayList<GalleryEnt> = ArrayList()
    //private var isSelection = false
    private lateinit var viewVModelMov: ViewModelGallery

    private var listAvi: ArrayList<GalleryEnt> = ArrayList()
    private var adaptorAvi: AviVidAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAviVidBinding.inflate(inflater, container, false)

        compressLayout = requireActivity().findViewById(R.id.bottom_layout_Vid)
        compressBtn = requireActivity().findViewById(R.id.compressed_layout_Vid)
        textCount = requireActivity().findViewById(R.id.text_Count_Vid)

        viewVModelMov = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelMov.getAllAvi().observe(requireActivity()) {
            adaptorAvi?.setData(it)
            if (it.isEmpty()){
                binding.noAviLayout.visibility = View.VISIBLE
            }
        }
        bottomSheetBehavior = BottomSheetBehavior.from(binding.includeBottomSheetVid.bottomSheet)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.recycleViewVideoAvi.setHasFixedSize(true)
        binding.recycleViewVideoAvi.layoutManager = LinearLayoutManager(requireContext())
        adaptorAvi = AviVidAdaptor(requireContext(), listAvi)
        binding.recycleViewVideoAvi.adapter = adaptorAvi

        compressBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.bottomSheetBGVid.visibility = View.VISIBLE
        }
        binding.includeBottomSheetVid.btnOk.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            fileName = binding.includeBottomSheetVid.filenameInput.text.toString()
            zipList(fileName)

            binding.bottomSheetBGVid.visibility = View.GONE
            hideKeyBoard(requireActivity())

            adaptorAvi?.setEditMode(false)
            isSelection = false

            //compressImage.visibility = View.GONE
            compressLayout.visibility = View.GONE
            for (n in listAvi.indices) { // For all list unchecked
                listAvi[n].isChecked = false
            }
        }

        binding.includeBottomSheetVid.btnCancel.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.bottomSheetBGVid.visibility = View.GONE

            adaptorAvi?.setEditMode(false)
            isSelection = false

            // compressImage.visibility = View.GONE
            compressLayout.visibility = View.GONE
            for (n in listAvi.indices) { // For all list unchecked
                listAvi[n].isChecked = false
            }
        }
        binding.bottomSheetBGVid.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.bottomSheetBGVid.visibility = View.GONE
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.bottomSheetBGVid.visibility = View.VISIBLE
                    compressLayout.visibility = View.GONE
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bottomSheetBGVid.visibility = View.GONE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })

        aviLongClick = {
            adaptorAvi?.setEditMode(true)
            adaptorAvi?.setChecked(it)
            isSelection = true

            // Update selectedCount
            /*  if (isSelection) {
                  selectedCount++
              } else {
                  selectedCount--
              }
              isSelection = !isSelection*/

            CompressDataClass.allImageList.addAll(adaptorAvi!!.getAllSelected())
            // Call callback method to update totalSelectedCount in MainActivity

            //selectedItemsList.addAll(selectedGallList)

            /* for (i in selectedGallList){
                 CompressDataClass.allImageList.add(i)
             }*/
            //textCount.text = CompressDataClass.allImageList.size.toString()

//           compressImage.visibility = View.VISIBLE
            compressLayout.visibility = View.VISIBLE
        }

        aviItemClick = {
            //getPosition = it
            if (isSelection){
                adaptorAvi?.setChecked(it)

                CompressDataClass.allImageList = adaptorAvi!!.getAllSelected()
                //selectedItemsList.addAll(selectedGallList)
                // textCount.text = CompressDataClass.allImageList.size.toString()
                //Toast.makeText(requireContext(), "Item Size are : ${selectedGallList.size}", Toast.LENGTH_SHORT).show()

                /*   // Update selectedCount
                   if (isSelection) {
                       selectedCount++
                   } else {
                       selectedCount--
                   }
                   isSelection = !isSelection*/

                // Call callback method to update totalSelectedCount in MainActivity

                /*  if (selectedGallList.isEmpty()){
                      imageAdaptor?.setEditMode(false)
                      isSelection = false

                     compressImage.visibility = View.GONE
                      compressLayout.visibility = View.GONE
                      for (n in galleryList.indices) { // For all list unchecked
                          galleryList[n].isChecked = false
                      }
                  }*/
            }
        }

        return binding.root
    }

    private fun zipList(name : String){
        val zipFiles = adaptorAvi?.getAllSelected()
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