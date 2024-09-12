package com.example.zipfilemanger.activities

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.zipfilemanger.R
import com.example.zipfilemanger.databinding.ActivityDocumentBinding
import com.example.zipfilemanger.documentFragments.ViewPagerDocumentAdapter
import com.example.zipfilemanger.imageFragment.adaptor.ViewPagerImageAdapter
import com.example.zipfilemanger.models.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator

class DocumentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDocumentBinding

    private var documentList: MutableList<CompressDataClass> = ArrayList()
    //private var compressAdaptor: DocumentAdaptor? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backDocuAct.setOnClickListener {
            onBackPressed()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.includeBottomSheetDocu.bottomSheet)
        this.bottomSheetPeek(action = {
            bottomSheetBehavior.peekHeight = 0
            /* if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                 binding.root.setBackgroundColor(Color.BLACK)
             }*/
        }, R.layout.fragment_all_audio, 0)
        //bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.viewPagerDocu.offscreenPageLimit = 1

        binding.viewPagerDocu.adapter = ViewPagerDocumentAdapter(this)
        TabLayoutMediator(binding.tabsDocu,binding.viewPagerDocu) { tab, index ->
            when (index) {
                0 -> {
                    tab.text = "ALL"
                    //tab.setIcon(R.drawable.ic_home)
                }
                1 -> {
                    tab.text = "WORD"
                    //tab.setIcon(R.drawable.ic_conversation)
                }
                2 -> {
                    tab.text = "EXCEL"
                    //tab.setIcon(R.drawable.ic_conversation)
                }
                3 -> {
                    tab.text = "PDF"
                    //tab.setIcon(R.drawable.ic_save_conversation)
                }
                4 -> {
                    tab.text = "PPT"
                    //tab.setIcon(R.drawable.ic_history)
                }
                5 -> {
                    tab.text = "TXT"
                    //tab.setIcon(R.drawable.ic_history)
                }

                else -> {
                    throw Resources.NotFoundException("Position Not Found")
                }
            }
        }.attach()

        binding.viewPagerDocu.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 ->{
                        binding.compressedLayoutDocu.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.includeBottomSheetDocu.btnOk.setOnClickListener {
                            //bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                            btnOkBottomSheetDocu?.invoke()
                            hideKeyBoard(this@DocumentActivity)

                            // if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            //  }
                            binding.bottomSheetBGDocu.visibility = View.GONE
                            binding.bottomLayoutDocu.visibility = View.GONE

                        }
                        binding.includeBottomSheetDocu.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGDocu.visibility = View.GONE

                            cancelBottomSheetDocu?.invoke()

                            binding.bottomLayoutDocu.visibility = View.GONE

                        }

                        binding.bottomSheetBGDocu.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGDocu.visibility = View.GONE
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
        binding.bottomSheetBGDocu.visibility = View.VISIBLE

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //binding.includeBottomSheetDocu.idTxtVu.text = "Convert To Zip"
                    binding.bottomSheetBGDocu.visibility = View.VISIBLE
                    binding.bottomLayoutDocu.visibility = View.GONE
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bottomSheetBGDocu.visibility = View.GONE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }

}