package com.example.zipfilemanger.activities

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.zipfilemanger.R
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.dataBase.ViewModelGallery
import com.example.zipfilemanger.adaptor.AudioAdaptor
import com.example.zipfilemanger.audioFragment.adaptor.ViewPagerAudioAdapter
import com.example.zipfilemanger.databinding.ActivityAudioBinding
import com.example.zipfilemanger.models.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator

class AudioActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAudioBinding

    //private var viewVModelG: ViewModelGallery
    //private var audioList : ArrayList<GalleryEnt> = ArrayList()

    //private var audioList: MutableList<CompressDataClass> = ArrayList()
    //private var audioAdaptor: AudioAdaptor? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backAudioAct.setOnClickListener {
            onBackPressed()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.includeBottomSheetAud.bottomSheet)
        this.bottomSheetPeek(action = {
            bottomSheetBehavior.peekHeight = 0
            /* if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                 binding.root.setBackgroundColor(Color.BLACK)
             }*/
        }, R.layout.fragment_all_audio, 0)
        //bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

     /*   viewVModelG = ViewModelProvider(this)[ViewModelGallery::class.java]
        viewVModelG.getAllAudio().observe(this){
            audioAdaptor?.setData(it)
        }*/

        binding.viewPagerAudio.offscreenPageLimit = 1

        binding.viewPagerAudio.adapter = ViewPagerAudioAdapter(this)
        TabLayoutMediator(binding.tabsAudio, binding.viewPagerAudio) { tab, index ->
            when (index) {
                0 -> {
                    tab.text = "All"
                    //tab.setIcon(R.drawable.ic_home)
                }
                1 -> {
                    tab.text = "Mp3"
                    //tab.setIcon(R.drawable.ic_conversation)
                }
                2 -> {
                    tab.text = "Mp4"
                    //tab.setIcon(R.drawable.ic_conversation)
                }
                3 -> {
                    tab.text = "Aac"
                    //tab.setIcon(R.drawable.ic_save_conversation)
                }
                else -> {
                    throw Resources.NotFoundException("Position Not Found")
                }
            }
        }.attach()

        binding.viewPagerAudio.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 ->{
                        binding.compressedLayoutAud.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.includeBottomSheetAud.btnOk.setOnClickListener {
                            //bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                            btnOkBottomSheetAud?.invoke()
                            hideKeyBoard(this@AudioActivity)

                            // if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            //  }
                            binding.bottomSheetBGAud.visibility = View.GONE
                            binding.bottomLayoutAud.visibility = View.GONE

                        }
                        binding.includeBottomSheetAud.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGAud.visibility = View.GONE

                            cancelBottomSheetAud?.invoke()

                            binding.bottomLayoutAud.visibility = View.GONE

                        }

                        binding.bottomSheetBGAud.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGAud.visibility = View.GONE
                        }

                    }
                    1 ->{

                    }
                    2 ->{

                    }
                    3 ->{

                    }
                    4 ->{

                    }
                }
            }
        })

    }

    private fun openBottomSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bottomSheetBGAud.visibility = View.VISIBLE

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.includeBottomSheetAud.idTxtVu.text = "Convert To Zip"
                    binding.bottomSheetBGAud.visibility = View.VISIBLE
                    binding.bottomLayoutAud.visibility = View.GONE
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bottomSheetBGAud.visibility = View.GONE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }

}