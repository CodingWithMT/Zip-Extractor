package com.example.zipfilemanger.imageFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.R
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.dataBase.ViewModelGallery
import com.example.zipfilemanger.databinding.FragmentPngImgBinding
import com.example.zipfilemanger.imageFragment.adaptor.*
import com.example.zipfilemanger.models.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*

class PngImgFragment : Fragment() {

    private var _binding: FragmentPngImgBinding? = null
    private val binding get() = _binding!!
    private var selectedPngList: ArrayList<GalleryEnt> = ArrayList()
    private var isSelection = false
    private lateinit var viewVModelJpg: ViewModelGallery
    private var listPng: ArrayList<GalleryEnt> = ArrayList()
    private var adaptorPng: PngImgAdaptor? = null
    var fileName = ""
    private lateinit var compressBtn : ConstraintLayout
    private lateinit var compressLayout : ConstraintLayout
    private lateinit var textCount : TextView

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPngImgBinding.inflate(inflater, container, false)

        compressLayout = requireActivity().findViewById(R.id.bottom_layout)
        textCount = requireActivity().findViewById(R.id.text_Count)
        compressBtn = requireActivity().findViewById(R.id.compressed_Btn)

        viewVModelJpg = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelJpg.getAllPng().observe(requireActivity()) {
            adaptorPng?.setData(it)

            if (it.isEmpty()){
                binding.noPngLayout.visibility = View.VISIBLE
            }
        }

        binding.recycleViewImagePng.setHasFixedSize(true)
        binding.recycleViewImagePng.setItemViewCacheSize(13)
        binding.recycleViewImagePng.layoutManager = LinearLayoutManager(requireContext())
        adaptorPng = PngImgAdaptor(requireContext(), listPng)
        binding.recycleViewImagePng.adapter = adaptorPng

        btnOkBottomSheetAll = {
            val includeBottom = requireActivity().findViewById<LinearLayout>(R.id.include_Bottom_SheetAll)
            fileName = includeBottom.filenameInput.text.toString()
            val allImageList = adaptorPng?.getAllSelected()

            if (it == "Zip"){
                compressInZip(fileName,allImageList!!,requireContext())
            }
            else{
                compressIn7Z(fileName,allImageList!!,requireContext())
            }
            for (n in listPng.indices) {
                listPng[n].isChecked = false
            }
            isSelection = false
            adaptorPng?.setEditMode(false)
            adaptorPng?.notifyDataSetChanged()
        }

        cancelBottomSheet = {
            if (isSelection){
                for (n in listPng.indices) {
                    listPng[n].isChecked = false
                }
                isSelection = false
                adaptorPng?.setEditMode(false)
            }
            adaptorPng?.notifyDataSetChanged()
        }

        pngLongClick = {
            adaptorPng?.setEditMode(true)
            adaptorPng?.setChecked(it)
            isSelection = true
            selectedPngList = adaptorPng!!.getAllSelected()
            textCount.text = "Items ${selectedPngList.size}"
            compressLayout.visibility = View.VISIBLE
        }

        pngItemClick = {
            if (isSelection){
                adaptorPng?.setChecked(it)
                selectedPngList = adaptorPng!!.getAllSelected()
                textCount.text = "Items ${selectedPngList.size}"
                if (selectedPngList.isEmpty()){
                    adaptorPng?.setEditMode(false)
                    isSelection = false
                    //compressImage.visibility = View.GONE
                    compressLayout.visibility = View.GONE
                    for (n in listPng.indices) { // For all list unchecked
                        listPng[n].isChecked = false
                    }
                }
            }
        }
        return binding.root
    }
}