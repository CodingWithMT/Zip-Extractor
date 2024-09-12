package com.example.zipfilemanger.audioFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.R
import com.example.zipfilemanger.audioFragment.adaptor.AllAudioAdaptor
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.dataBase.ViewModelGallery
import com.example.zipfilemanger.databinding.FragmentAllAudioBinding
import com.example.zipfilemanger.databinding.FragmentMp3AudBinding
import com.example.zipfilemanger.databinding.FragmentMp4AudBinding

class Mp4AudFragment : Fragment() {


    private var _binding: FragmentMp4AudBinding? = null
    private val binding get() = _binding!!

    private var selectedGallList: ArrayList<GalleryEnt> = ArrayList()
    private var isSelection = false
    private lateinit var viewVModelAud: ViewModelGallery

    private var audioList: ArrayList<GalleryEnt> = ArrayList()
    private var audioAdaptor: AllAudioAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMp4AudBinding.inflate(inflater, container, false)

        viewVModelAud = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelAud.getAllAudMp4().observe(requireActivity()) {
            audioAdaptor?.setData(it)
            if (it.isEmpty()){
                binding.noMp4AudLayout.visibility = View.VISIBLE
            }
        }

        binding.recycleViewAudioMp4.setHasFixedSize(true)
        binding.recycleViewAudioMp4.layoutManager = LinearLayoutManager(requireContext())
        audioAdaptor = AllAudioAdaptor(requireContext(), audioList)
        binding.recycleViewAudioMp4.adapter = audioAdaptor

        return binding.root
    }
}