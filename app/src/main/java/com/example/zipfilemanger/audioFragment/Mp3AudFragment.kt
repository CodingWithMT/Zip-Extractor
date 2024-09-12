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

class Mp3AudFragment : Fragment() {

    private var _binding: FragmentMp3AudBinding? = null
    private val binding get() = _binding!!

    //private var selectedGallList: ArrayList<GalleryEnt> = ArrayList()
    //private var isSelection = false
    private lateinit var viewVModelAud: ViewModelGallery

    private var audioList: ArrayList<GalleryEnt> = ArrayList()
    private var audioAdaptor: AllAudioAdaptor? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMp3AudBinding.inflate(inflater, container, false)

        viewVModelAud = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelAud.getAllAudMp3().observe(requireActivity()) {
            audioAdaptor?.setData(it)
            if (it.isEmpty()){
                binding.noMp3Layout.visibility = View.VISIBLE
            }
        }

        binding.recycleViewAudioMp3.setHasFixedSize(true)
        binding.recycleViewAudioMp3.layoutManager = LinearLayoutManager(requireContext())
        audioAdaptor = AllAudioAdaptor(requireContext(), audioList)
        binding.recycleViewAudioMp3.adapter = audioAdaptor

        return binding.root
    }
}