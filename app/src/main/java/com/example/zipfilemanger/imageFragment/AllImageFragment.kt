package com.example.zipfilemanger.imageFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.R
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.dataBase.ViewModelGallery
import com.example.zipfilemanger.databinding.FragmentAllImageBinding
import com.example.zipfilemanger.imageFragment.adaptor.AllImageAdaptor
import com.example.zipfilemanger.imageFragment.adaptor.galleryItemClick
import com.example.zipfilemanger.imageFragment.adaptor.galleryLongClick
import com.example.zipfilemanger.models.btnOkBottomSheetAll
import com.example.zipfilemanger.models.cancelBottomSheet
import com.example.zipfilemanger.models.compressIn7Z
import com.example.zipfilemanger.models.compressInZip
import kotlinx.android.synthetic.main.bottom_sheet.view.*

class AllImageFragment : Fragment() {
    private var _binding: FragmentAllImageBinding? = null
    private val binding get() = _binding!!

    private var selectedGallList: ArrayList<GalleryEnt> = ArrayList()
    private var isSelection = false
    private lateinit var viewVModelG: ViewModelGallery
    private var listGallery: ArrayList<GalleryEnt> = ArrayList()
    private var imageAdaptor: AllImageAdaptor? = null

    private var fileName = ""
    private lateinit var compressLayout : ConstraintLayout
    private lateinit var textCount : TextView
    private lateinit var bGBottomSheet : View


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllImageBinding.inflate(inflater, container, false)

        compressLayout = requireActivity().findViewById(R.id.bottom_layout)
        bGBottomSheet = requireActivity().findViewById(R.id.bottomSheetBG)
        textCount = requireActivity().findViewById(R.id.text_Count)

        viewVModelG = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelG.getAllImage().observe(requireActivity()) {
            imageAdaptor?.setData(it)
        }

        binding.recycleViewImageAll.setHasFixedSize(true)
        binding.recycleViewImageAll.layoutManager = LinearLayoutManager(requireContext())
        imageAdaptor = AllImageAdaptor(requireContext(), listGallery)
        binding.recycleViewImageAll.adapter = imageAdaptor

        btnOkBottomSheetAll = {
            val includeBottom = requireActivity().findViewById<LinearLayout>(R.id.include_Bottom_SheetAll)
            fileName = includeBottom.filenameInput.text.toString()
            val allImageList = imageAdaptor?.getAllSelected()

            if (it == "Zip"){
                compressInZip(fileName,allImageList!!,requireContext())
            }
            else{
                compressIn7Z(fileName,allImageList!!,requireContext())
            }
            for (n in listGallery.indices) {
                    listGallery[n].isChecked = false
            }
            isSelection = false
            imageAdaptor?.setEditMode(false)
            imageAdaptor?.notifyDataSetChanged()
        }

        cancelBottomSheet = {
           if (isSelection){
               for (n in listGallery.indices) {
                       listGallery[n].isChecked = false
                   }
               isSelection = false
               imageAdaptor?.setEditMode(false)
           }
            imageAdaptor?.notifyDataSetChanged()
        }

        galleryLongClick = {
            imageAdaptor?.setEditMode(true)
            imageAdaptor?.setChecked(it)
            isSelection = true
            selectedGallList = imageAdaptor!!.getAllSelected()
            textCount.text = "Items ${selectedGallList.size}"
            compressLayout.visibility = View.VISIBLE
        }

        galleryItemClick = {
            if (isSelection){
                imageAdaptor?.setChecked(it)
                selectedGallList = imageAdaptor!!.getAllSelected()
                textCount.text = "Items ${selectedGallList.size}"
            }
        }
        return binding.root
    }
}