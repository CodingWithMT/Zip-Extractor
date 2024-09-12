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
import com.example.zipfilemanger.databinding.FragmentGifImgBinding
import com.example.zipfilemanger.imageFragment.adaptor.*
import com.example.zipfilemanger.models.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*

class GifImgFragment : Fragment() {
    private var _binding: FragmentGifImgBinding? = null
    private val binding get() = _binding!!

    private var selectedGiffList: ArrayList<GalleryEnt> = ArrayList()
    private var isSelection = false
    private lateinit var viewVModelGif: ViewModelGallery
    private var listGif: ArrayList<GalleryEnt> = ArrayList()
    private var adaptorGif: GifImgAdaptor? = null
    var fileName = ""
    private lateinit var compressBtnGif : ConstraintLayout
    private lateinit var compressLayout : ConstraintLayout
    private lateinit var textCount : TextView

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGifImgBinding.inflate(inflater, container, false)

        compressLayout = requireActivity().findViewById(R.id.bottom_layout)
        textCount = requireActivity().findViewById(R.id.text_Count)
        compressBtnGif = requireActivity().findViewById(R.id.compressed_Btn)

        viewVModelGif = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelGif.getAllGif().observe(requireActivity()) {
            adaptorGif?.setData(it)

            if (it.isEmpty()){
                binding.noGifLayout.visibility = View.VISIBLE
            }
        }

        binding.recycleViewImageGif.setHasFixedSize(true)
        binding.recycleViewImageGif.setItemViewCacheSize(13)
        binding.recycleViewImageGif.layoutManager = LinearLayoutManager(requireContext())
        adaptorGif = GifImgAdaptor(requireContext(),listGif)
        binding.recycleViewImageGif.adapter = adaptorGif

        btnOkBottomSheetAll = {
            val includeBottom = requireActivity().findViewById<LinearLayout>(R.id.include_Bottom_SheetAll)
            fileName = includeBottom.filenameInput.text.toString()
            val allImageList = adaptorGif?.getAllSelected()

            if (it == "Zip"){
                compressInZip(fileName,allImageList!!,requireContext())
            }
            else{
                compressIn7Z(fileName,allImageList!!,requireContext())
            }
            for (n in listGif.indices) {
                listGif[n].isChecked = false
            }
            isSelection = false
            adaptorGif?.setEditMode(false)
            adaptorGif?.notifyDataSetChanged()
        }

        cancelBottomSheet = {
            if (isSelection){
                for (n in listGif.indices) {
                    listGif[n].isChecked = false
                }
                isSelection = false
                adaptorGif?.setEditMode(false)
            }
            adaptorGif?.notifyDataSetChanged()
        }

        gifLongClick = {
            adaptorGif?.setEditMode(true)
            adaptorGif?.setChecked(it)
            isSelection = true
            selectedGiffList = adaptorGif!!.getAllSelected()
            textCount.text = "Items ${selectedGiffList.size}"

            //compressImage.visibility = View.VISIBLE
            compressLayout.visibility = View.VISIBLE
        }

        gifItemClick = {
            if (isSelection){
                adaptorGif?.setChecked(it)
                selectedGiffList = adaptorGif!!.getAllSelected()
                textCount.text = "Items ${selectedGiffList.size}"

                if (selectedGiffList.isEmpty()){
                    adaptorGif?.setEditMode(false)
                    isSelection = false
                    compressLayout.visibility = View.GONE
                    for (n in listGif.indices) { // For all list unchecked
                        listGif[n].isChecked = false
                    }
                }
            }
        }
        return binding.root
    }
}