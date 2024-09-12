package com.example.zipfilemanger.videosFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.dataBase.ViewModelGallery
import com.example.zipfilemanger.databinding.FragmentMp4VidBinding
import com.example.zipfilemanger.videosFragment.adaptor.Mp4VidAdaptor


class Mp4VidFragment : Fragment() {

    private var _binding: FragmentMp4VidBinding? = null
    private val binding get() = _binding!!

    //private var selectedGallList: ArrayList<GalleryEnt> = ArrayList()
    //private var isSelection = false
    private lateinit var viewVModelMp4: ViewModelGallery

    private var mp4List: ArrayList<GalleryEnt> = ArrayList()
    private var mp4Adaptor: Mp4VidAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMp4VidBinding.inflate(inflater, container, false)

        viewVModelMp4 = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelMp4.getAllMp4().observe(requireActivity()) {
            mp4Adaptor?.setData(it)
            if (it.isEmpty()){
                binding.noMp4Layout.visibility = View.VISIBLE
            }
        }

        binding.recycleViewVideoMp4.setHasFixedSize(true)
        binding.recycleViewVideoMp4.layoutManager = LinearLayoutManager(requireContext())
        mp4Adaptor = Mp4VidAdaptor(requireContext(), mp4List)
        binding.recycleViewVideoMp4.adapter = mp4Adaptor

        return binding.root
    }
}