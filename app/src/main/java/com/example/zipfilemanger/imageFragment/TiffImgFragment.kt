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
import com.example.zipfilemanger.databinding.FragmentTiffImgBinding
import com.example.zipfilemanger.imageFragment.adaptor.*
import com.example.zipfilemanger.models.btnOkBottomSheetAll
import com.example.zipfilemanger.models.cancelBottomSheet
import com.example.zipfilemanger.models.compressIn7Z
import com.example.zipfilemanger.models.compressInZip
import kotlinx.android.synthetic.main.bottom_sheet.view.*

class TiffImgFragment : Fragment() {

    private var _binding: FragmentTiffImgBinding? = null
    private val binding get() = _binding!!
    private var selectedTiffList: ArrayList<GalleryEnt> = ArrayList()
    private var isSelection = false
    private lateinit var viewVModelJpg: ViewModelGallery
    private var listTiff: ArrayList<GalleryEnt> = ArrayList()
    private var adaptorTiff: TiffImgAdaptor? = null
    var fileName = ""
    private lateinit var compressBtn : ConstraintLayout
    private lateinit var compressLayout : ConstraintLayout
    private lateinit var textCount : TextView

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTiffImgBinding.inflate(inflater, container, false)

        compressLayout = requireActivity().findViewById(R.id.bottom_layout)
        textCount = requireActivity().findViewById(R.id.text_Count)
        compressBtn = requireActivity().findViewById(R.id.compressed_Btn)

        viewVModelJpg = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelJpg.getAllTiff().observe(requireActivity()) {
            adaptorTiff?.setData(it)
            if (it.isEmpty()){
                binding.noDataLayout.visibility = View.VISIBLE
            }
        }

    /*    bottomSheetBehavior = BottomSheetBehavior.from(binding.includeBottomSheet.bottomSheet)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED*/

        binding.recycleViewImageTiff.setHasFixedSize(true)
        binding.recycleViewImageTiff.setItemViewCacheSize(13)
        binding.recycleViewImageTiff.layoutManager = LinearLayoutManager(requireContext())
        adaptorTiff = TiffImgAdaptor(requireContext(), listTiff)
        binding.recycleViewImageTiff.adapter = adaptorTiff

        btnOkBottomSheetAll = {
            val includeBottom = requireActivity().findViewById<LinearLayout>(R.id.include_Bottom_SheetAll)
            fileName = includeBottom.filenameInput.text.toString()
            val allImageList = adaptorTiff?.getAllSelected()

            if (it == "Zip"){
                compressInZip(fileName,allImageList!!,requireContext())
            }
            else{
                compressIn7Z(fileName,allImageList!!,requireContext())
            }

            for (n in listTiff.indices) {
                listTiff[n].isChecked = false
            }
            isSelection = false
            adaptorTiff?.setEditMode(false)
            adaptorTiff?.notifyDataSetChanged()
        }

        cancelBottomSheet = {
            if (isSelection){
                for (n in listTiff.indices) {
                    listTiff[n].isChecked = false
                }
                isSelection = false
                adaptorTiff?.setEditMode(false)
            }
            adaptorTiff?.notifyDataSetChanged()
        }

        tiffLongClick = {
            adaptorTiff?.setEditMode(true)
            adaptorTiff?.setChecked(it)
            isSelection = true
            selectedTiffList = adaptorTiff!!.getAllSelected()
            textCount.text = "Items ${selectedTiffList.size}"
            compressLayout.visibility = View.VISIBLE
        }

        tiffItemClick = {
            if (isSelection){
                adaptorTiff?.setChecked(it)
                selectedTiffList = adaptorTiff!!.getAllSelected()
                textCount.text = "Items ${selectedTiffList.size}"

                if (selectedTiffList.isEmpty()){
                    adaptorTiff?.setEditMode(false)
                    isSelection = false
                    compressLayout.visibility = View.GONE
                    for (n in listTiff.indices) { // For all list unchecked
                        listTiff[n].isChecked = false
                    }
                }
            }
        }
        return binding.root
    }
}