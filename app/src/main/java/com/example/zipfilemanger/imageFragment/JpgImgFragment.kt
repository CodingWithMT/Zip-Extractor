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
import com.example.zipfilemanger.databinding.FragmentJpgImgBinding
import com.example.zipfilemanger.imageFragment.adaptor.*
import com.example.zipfilemanger.models.btnOkBottomSheetAll
import com.example.zipfilemanger.models.cancelBottomSheet
import com.example.zipfilemanger.models.compressIn7Z
import com.example.zipfilemanger.models.compressInZip
import kotlinx.android.synthetic.main.bottom_sheet.view.*

class JpgImgFragment : Fragment() {

    private var _binding: FragmentJpgImgBinding? = null
    private val binding get() = _binding!!
    private var selectedJpgList: ArrayList<GalleryEnt> = ArrayList()
    private var isSelection = false
    private lateinit var viewVModelJpg: ViewModelGallery
    private var listJpg: ArrayList<GalleryEnt> = ArrayList()
    private var adaptorJpg: JpgImgAdaptor? = null
    var fileName = ""
    private lateinit var compressBtn : ConstraintLayout
    private lateinit var compressLayout : ConstraintLayout
    private lateinit var textCount : TextView

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentJpgImgBinding.inflate(inflater, container, false)

        compressLayout = requireActivity().findViewById(R.id.bottom_layout)
        compressBtn = requireActivity().findViewById(R.id.compressed_Btn)
        textCount = requireActivity().findViewById(R.id.text_Count)

        viewVModelJpg = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelJpg.getAllJpg().observe(requireActivity()) {
            adaptorJpg?.setData(it)
        }

/*        bottomSheetBehavior = BottomSheetBehavior.from(binding.includeBottomSheet.bottomSheet)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED*/

        binding.recycleViewImageJpg.setHasFixedSize(true)
        binding.recycleViewImageJpg.layoutManager = LinearLayoutManager(requireContext())
        adaptorJpg = JpgImgAdaptor(requireContext(), listJpg)
        binding.recycleViewImageJpg.adapter = adaptorJpg

        btnOkBottomSheetAll = {
            val includeBottom = requireActivity().findViewById<LinearLayout>(R.id.include_Bottom_SheetAll)
            fileName = includeBottom.filenameInput.text.toString()
            val allImageList = adaptorJpg?.getAllSelected()

            if (it == "Zip"){
                compressInZip(fileName,allImageList!!,requireContext())
            }
            else{
                compressIn7Z(fileName,allImageList!!,requireContext())
            }

            for (n in listJpg.indices) {
                listJpg[n].isChecked = false
            }
            isSelection = false
            adaptorJpg?.setEditMode(false)
            adaptorJpg?.notifyDataSetChanged()
        }

        cancelBottomSheet = {
            if (isSelection){
                for (n in listJpg.indices) {
                    listJpg[n].isChecked = false
                }
                isSelection = false
                adaptorJpg?.setEditMode(false)
            }
            adaptorJpg?.notifyDataSetChanged()
        }

        jpgLongClick = {
            adaptorJpg?.setEditMode(true)
            adaptorJpg?.setChecked(it)
            isSelection = true

            selectedJpgList = adaptorJpg!!.getAllSelected()
            textCount.text = "Items ${selectedJpgList.size}"
            compressLayout.visibility = View.VISIBLE
        }

        jpgItemClick = {
            if (isSelection){
                adaptorJpg?.setChecked(it)
                selectedJpgList = adaptorJpg!!.getAllSelected()
                textCount.text = "Items ${selectedJpgList.size}"

                if (selectedJpgList.isEmpty()){
                    adaptorJpg?.setEditMode(false)
                    isSelection = false
                    compressLayout.visibility = View.GONE
                    for (n in listJpg.indices) { // For all list unchecked
                        listJpg[n].isChecked = false
                    }
                }
            }
        }
        return binding.root
    }
}