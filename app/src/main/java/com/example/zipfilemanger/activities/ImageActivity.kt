package com.example.zipfilemanger.activities

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.zipfilemanger.R
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.databinding.ActivityImageBinding
import com.example.zipfilemanger.imageFragment.adaptor.*
import com.example.zipfilemanger.models.bottomSheetPeek
import com.example.zipfilemanger.models.btnOkBottomSheetAll
import com.example.zipfilemanger.models.cancelBottomSheet
import com.example.zipfilemanger.models.hideKeyBoard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator

class ImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageBinding
    //private var selectedGallList: ArrayList<GalleryEnt> = ArrayList()
    private var isSelection = false
    private var galleryList: ArrayList<GalleryEnt> = ArrayList()
    private var imageAdaptor: AllImageAdaptor? = null
    private var zipString = ""
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImageAct.setOnClickListener {
            onBackPressed()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.includeBottomSheetAll.bottomSheet)
        this.bottomSheetPeek(action = {
            bottomSheetBehavior.peekHeight = 0
            /* if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                 binding.root.setBackgroundColor(Color.BLACK)
             }*/
        }, R.layout.fragment_all_image, 0)
        //bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.viewPagerImage.adapter = ViewPagerImageAdapter(this)
        TabLayoutMediator(binding.tabsImage, binding.viewPagerImage) { tab, index ->
            when (index) {
                0 -> {
                    tab.text = "All"
                    //tab.setIcon(R.drawable.ic_home)
                }
                1 -> {
                    tab.text = "Jpg"
                    //tab.setIcon(R.drawable.ic_conversation)
                }
                2 -> {
                    tab.text = "Tiff"
                    //tab.setIcon(R.drawable.ic_conversation)
                }
                3 -> {
                    tab.text = "Png"
                    //tab.setIcon(R.drawable.ic_save_conversation)
                }
                4 -> {
                    tab.text = "Gif"
                    //tab.setIcon(R.drawable.ic_history)
                }
                else -> {
                    throw Resources.NotFoundException("Position Not Found")
                }
            }
        }.attach()

        binding.viewPagerImage.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.compressedBtn.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.moreLayout.setOnClickListener {
                            //openBottomSheet7Z()
                        }

                        binding.includeBottomSheetAll.btnOk.setOnClickListener {

                            btnOkBottomSheetAll?.invoke(zipString)
                            hideKeyBoard(this@ImageActivity)
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                            binding.bottomLayout.visibility = View.GONE
                        }

                        binding.includeBottomSheetAll.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                            cancelBottomSheet?.invoke()
                            binding.bottomLayout.visibility = View.GONE
                        }

                        binding.bottomSheetBG.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                        }
                    }
                    1 -> {

                        binding.compressedBtn.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.includeBottomSheetAll.btnOk.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            btnOkBottomSheetAll?.invoke(zipString)
                            hideKeyBoard(this@ImageActivity)
                            imageAdaptor?.setEditMode(false)
                            isSelection = false

                            binding.bottomSheetBG.visibility = View.GONE
                            binding.bottomLayout.visibility = View.GONE
                            for (n in galleryList.indices) { // For all list unchecked
                                galleryList[n].isChecked = false
                            }
                        }

                        binding.includeBottomSheetAll.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                            cancelBottomSheet?.invoke()
                        }

                        binding.bottomSheetBG.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                        }
                    }
                    2 -> {

                        binding.compressedBtn.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.includeBottomSheetAll.btnOk.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            btnOkBottomSheetAll?.invoke(zipString)
                            hideKeyBoard(this@ImageActivity)
                            imageAdaptor?.setEditMode(false)
                            isSelection = false
                            binding.bottomSheetBG.visibility = View.GONE
                            binding.bottomLayout.visibility = View.GONE
                            for (n in galleryList.indices) { // For all list unchecked
                                galleryList[n].isChecked = false
                            }
                        }

                        binding.includeBottomSheetAll.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                            cancelBottomSheet?.invoke()
                        }

                        binding.bottomSheetBG.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                        }
                    }
                    3 -> {

                        binding.compressedBtn.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.includeBottomSheetAll.btnOk.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            btnOkBottomSheetAll?.invoke(zipString)
                            hideKeyBoard(this@ImageActivity)
                            imageAdaptor?.setEditMode(false)
                            isSelection = false
                            binding.bottomSheetBG.visibility = View.GONE
                            binding.bottomLayout.visibility = View.GONE
                            for (n in galleryList.indices) { // For all list unchecked
                                galleryList[n].isChecked = false
                            }
                        }

                        binding.includeBottomSheetAll.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                            cancelBottomSheet?.invoke()
                        }

                        binding.bottomSheetBG.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                        }
                    }
                    4 -> {
                        binding.compressedBtn.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.includeBottomSheetAll.btnOk.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            btnOkBottomSheetAll?.invoke(zipString)
                            hideKeyBoard(this@ImageActivity)
                            imageAdaptor?.setEditMode(false)
                            isSelection = false
                            //compressImage.visibility = View.GONE
                            binding.bottomSheetBG.visibility = View.GONE
                            binding.bottomLayout.visibility = View.GONE
                            for (n in galleryList.indices) { // For all list unchecked
                                galleryList[n].isChecked = false
                            }
                        }

                        binding.includeBottomSheetAll.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                            cancelBottomSheet?.invoke()
                        }

                        binding.bottomSheetBG.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                        }
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

    private fun openBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bottomSheetBG.visibility = View.VISIBLE

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.bottomSheetBG.visibility = View.VISIBLE
                    binding.bottomLayout.visibility = View.GONE
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bottomSheetBG.visibility = View.GONE
                    hideKeyBoard(this@ImageActivity)
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }
}
