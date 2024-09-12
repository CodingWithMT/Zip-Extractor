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
import com.example.zipfilemanger.databinding.FragmentMovVidBinding
import com.example.zipfilemanger.videosFragment.adaptor.MovVidAdaptor
import com.example.zipfilemanger.videosFragment.adaptor.Mp4VidAdaptor


class MovVidFragment : Fragment() {
    private var _binding: FragmentMovVidBinding? = null
    private val binding get() = _binding!!

    //private var selectedGallList: ArrayList<GalleryEnt> = ArrayList()
    //private var isSelection = false
    private lateinit var viewVModelMov: ViewModelGallery

    private var movList: ArrayList<GalleryEnt> = ArrayList()
    private var movAdaptor: MovVidAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMovVidBinding.inflate(inflater, container, false)

        viewVModelMov = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelMov.getAllMov().observe(requireActivity()) {
            movAdaptor?.setData(it)
            if (it.isEmpty()){
                binding.noMovLayout.visibility = View.VISIBLE
            }
        }

        binding.recycleViewVideoMov.setHasFixedSize(true)
        binding.recycleViewVideoMov.layoutManager = LinearLayoutManager(requireContext())
        movAdaptor = MovVidAdaptor(requireContext(), movList)
        binding.recycleViewVideoMov.adapter = movAdaptor

        return binding.root
    }
}