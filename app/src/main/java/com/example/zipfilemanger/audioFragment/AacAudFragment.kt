package com.example.zipfilemanger.audioFragment

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
import com.example.zipfilemanger.audioFragment.adaptor.AllAudioAdaptor
import com.example.zipfilemanger.audioFragment.adaptor.audioItemClick
import com.example.zipfilemanger.audioFragment.adaptor.audioLongClick
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.dataBase.ViewModelGallery
import com.example.zipfilemanger.databinding.FragmentAacAudBinding
import com.example.zipfilemanger.databinding.FragmentMp3AudBinding
import com.example.zipfilemanger.models.*
import com.example.zipfilemanger.videosFragment.adaptor.allVidItemClick
import com.example.zipfilemanger.videosFragment.adaptor.allVidLongClick
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class AacAudFragment : Fragment() {

    private var _binding: FragmentAacAudBinding? = null
    private val binding get() = _binding!!

    private var selectedAudList: ArrayList<GalleryEnt> = ArrayList()
    private var isSelection = false
    private lateinit var viewVModelAud: ViewModelGallery

    var fileName = ""
    private lateinit var bottomSheetBehaviorAud: BottomSheetBehavior<LinearLayout>
    private lateinit var compressLayout : ConstraintLayout
    private lateinit var textCount : TextView
    private lateinit var compressBtn : ConstraintLayout

    private var audioList: ArrayList<GalleryEnt> = ArrayList()
    private var audioAdaptor: AllAudioAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAacAudBinding.inflate(inflater, container, false)

        compressLayout = requireActivity().findViewById(R.id.bottom_layout_Aud)
        compressBtn = requireActivity().findViewById(R.id.compressed_layout_Aud)
        textCount = requireActivity().findViewById(R.id.text_Count_Aud)

        viewVModelAud = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelAud.getAllAudAAC().observe(requireActivity()) {
            audioAdaptor?.setData(it)
            if (it.isEmpty()){
                binding.noAacLayout.visibility = View.VISIBLE
            }
        }

        binding.recycleViewAudioAac.setHasFixedSize(true)
        binding.recycleViewAudioAac.layoutManager = LinearLayoutManager(requireContext())
        audioAdaptor = AllAudioAdaptor(requireContext(), audioList)
        binding.recycleViewAudioAac.adapter = audioAdaptor


   /*     audioLongClick = {
            audioAdaptor?.setEditMode(true)
            audioAdaptor?.setChecked(it)
            isSelection = true

            selectedAudList = audioAdaptor!!.getAllSelected()
            textCount.text = "Items ${selectedAudList.size}"

            compressLayout.visibility = View.VISIBLE

            textCount.text = CompressDataClass.allImageList.size.toString()
//           compressImage.visibility = View.VISIBLE
            compressLayout.visibility = View.VISIBLE
        }

        audioItemClick = {
            //getPosition = it
            if (isSelection){
                audioAdaptor?.setChecked(it)

                selectedAudList = audioAdaptor!!.getAllSelected()
                textCount.text = "Items ${selectedAudList.size}"

                compressLayout.visibility = View.VISIBLE
            }
        }

        btnOkBottomSheetAud = {
            val includeBottom = requireActivity().findViewById<LinearLayout>(R.id.include_Bottom_Sheet_Vid)
            fileName = includeBottom.filenameInput.text.toString()
            zipList(fileName)

            for (n in audioList.indices) {
                audioList[n].isChecked = false
            }
            isSelection = false
            audioAdaptor?.setEditMode(false)
            audioAdaptor?.notifyDataSetChanged()
        }

        cancelBottomSheetAud = {
            if (isSelection){
                for (n in audioList.indices) {
                    audioList[n].isChecked = false
                }
                isSelection = false
                audioAdaptor?.setEditMode(false)
            }
            audioAdaptor?.notifyDataSetChanged()
        }*/

        return binding.root
    }

    private fun zipList(name : String){
        val zipFiles = audioAdaptor?.getAllSelected()
        val fileList: MutableList<File> = mutableListOf()
        for (i in zipFiles!!) {
            val f = File(i.fPath)
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