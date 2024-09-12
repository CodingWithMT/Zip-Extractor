package com.example.zipfilemanger.activities

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.zipfilemanger.R
import com.example.zipfilemanger.databinding.ActivityVideoBinding
import com.example.zipfilemanger.models.*
import com.example.zipfilemanger.videosFragment.adaptor.ViewPagerVideoAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator

class VideoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVideoBinding

    private var zipString = ""

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backVideoAct.setOnClickListener {
            onBackPressed()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.includeBottomSheetVid.bottomSheet)
        this.bottomSheetPeek(action = {
            bottomSheetBehavior.peekHeight = 0
        }, R.layout.fragment_all_image, 0)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.viewPagerVideo.offscreenPageLimit = 1

        binding.viewPagerVideo.adapter = ViewPagerVideoAdapter(this)
        TabLayoutMediator(binding.tabsVideo, binding.viewPagerVideo) { tab, index ->
            when (index) {
                0 -> {
                    tab.text = "All"
                    //tab.setIcon(R.drawable.ic_home)
                }
                1 -> {
                    tab.text = "Mp4"
                    //tab.setIcon(R.drawable.ic_conversation)
                }
                2 -> {
                    tab.text = "Mov"
                    //tab.setIcon(R.drawable.ic_conversation)
                }
                3 -> {
                    tab.text = "Avi"
                    //tab.setIcon(R.drawable.ic_save_conversation)
                }
                else -> {
                    throw Resources.NotFoundException("Position Not Found")
                }
            }
        }.attach()

        binding.viewPagerVideo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 ->{
                        binding.compressedLayoutVid.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.includeBottomSheetVid.btnOk.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            btnOkBottomSheetVid?.invoke()
                            hideKeyBoard(this@VideoActivity)
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGVid.visibility = View.GONE
                            binding.bottomLayoutVid.visibility = View.GONE
                        }
                        
                        binding.includeBottomSheetVid.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGVid.visibility = View.GONE
                            cancelBottomSheetVid?.invoke()
                            binding.bottomLayoutVid.visibility = View.GONE
                        }

                        binding.bottomSheetBGVid.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGVid.visibility = View.GONE
                        }
                    }
                    1 ->{

                        binding.compressedLayoutVid.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.includeBottomSheetVid.btnOk.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            btnOkBottomSheetAll?.invoke(zipString)
                            hideKeyBoard(this@VideoActivity)
                            binding.bottomSheetBGVid.visibility = View.GONE
                            binding.bottomLayoutVid.visibility = View.GONE
                        }

                        binding.includeBottomSheetVid.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGVid.visibility = View.GONE
                            cancelBottomSheet?.invoke()
                        }

                        binding.bottomSheetBGVid.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGVid.visibility = View.GONE
                        }
                    }
                    2 ->{

                        binding.compressedLayoutVid.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.includeBottomSheetVid.btnOk.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            btnOkBottomSheetAll?.invoke(zipString)
                            hideKeyBoard(this@VideoActivity)
                            binding.bottomSheetBGVid.visibility = View.GONE
                            binding.bottomLayoutVid.visibility = View.GONE
                        }

                        binding.includeBottomSheetVid.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGVid.visibility = View.GONE
                            cancelBottomSheet?.invoke()
                        }

                        binding.bottomSheetBGVid.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGVid.visibility = View.GONE
                        }
                    }
                    3 ->{

                    }
                    4 ->{

                    }
                }
            }
        })
    }

    //For Radio Btn Click
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.zipButton ->
                    if (checked) {
                        zipString = "Zip"
                        // Pirates are the best
                        Toast.makeText(this, "Zip", Toast.LENGTH_SHORT).show()
                    }
                R.id.sevenZButton ->
                    if (checked) {
                        // Ninjas rule
                        zipString = "7z"
                        Toast.makeText(this, "7Z", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun openBottomSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bottomSheetBGVid.visibility = View.VISIBLE

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SetTextI18n")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.includeBottomSheetVid.idTxtVu.text = "Convert To Zip"
                    binding.bottomSheetBGVid.visibility = View.VISIBLE
                    binding.bottomLayoutVid.visibility = View.GONE
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bottomSheetBGVid.visibility = View.GONE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }

}