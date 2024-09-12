package com.example.zipfilemanger.activities

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.zipfilemanger.R
import com.example.zipfilemanger.compressFragments.*
import com.example.zipfilemanger.databinding.ActivityCompressBinding
import com.example.zipfilemanger.models.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class CompressActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCompressBinding

    private var selectedGallList: ArrayList<CompressDataClass> = ArrayList()
    private var isSelection = false
    //private latent var viewVModelGCompress: ViewModelGallery

    private var compressList: MutableList<CompressDataClass> = ArrayList()
    private var compressAdaptor: CompressedAdaptor? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompressBinding.inflate(layoutInflater)
        setContentView(binding.root)


        bottomSheetBehavior = BottomSheetBehavior.from(binding.includeBottomSheetZip.bottomSheet)
        this.bottomSheetPeek(action = {
            bottomSheetBehavior.peekHeight = 0
            /* if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                 binding.root.setBackgroundColor(Color.BLACK)
             }*/
        }, R.layout.fragment_all_image, 0)
        //bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.backCompressAct.setOnClickListener {
            onBackPressed()
        }

        binding.viewPagerCompress.offscreenPageLimit = 1

        binding.viewPagerCompress.adapter = ViewPagerCompressAdapter(this)
        TabLayoutMediator(binding.tabsCompress, binding.viewPagerCompress) { tab, index ->
            when (index) {
                0 -> {
                    tab.text = "Zip"
                    //tab.setIcon(R.drawable.ic_home)
                }
                1 -> {
                    tab.text = "7z"
                    //tab.setIcon(R.drawable.ic_conversation)
                }
                2 -> {
                    tab.text = "Tar"
                    //tab.setIcon(R.drawable.ic_conversation)
                }
                3 -> {
                    tab.text = "Rar"
                    //tab.setIcon(R.drawable.ic_save_conversation)
                }
                else -> {
                    throw Resources.NotFoundException("Position Not Found")
                }
            }
        }.attach()



        binding.viewPagerCompress.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 ->{
                        binding.extractLayout.setOnClickListener {
                            openBottomSheet()
                        }

                        binding.includeBottomSheetZip.btnOk.setOnClickListener {
                            //bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                            btnOkBottomSheetZip?.invoke()
                            hideKeyBoard(this@CompressActivity)

                            // if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            //  }
                            binding.bottomSheetBGZip.visibility = View.GONE
                            binding.bottomLayoutZip.visibility = View.GONE

                        }
                        binding.includeBottomSheetZip.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGZip.visibility = View.GONE

                            cancelBottomSheetZip?.invoke()

                            binding.bottomLayoutZip.visibility = View.GONE

                        }

                        binding.bottomSheetBGZip.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBGZip.visibility = View.GONE
                        }

                    }
                    1 ->{
                   /*     binding.compressedBtn.setOnClickListener {
                            openBottomSheet()
                        }
                        binding.includeBottomSheetAll.btnOk.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            fileName = binding.includeBottomSheetAll.filenameInput.text.toString()
                            zipListJpg(fileName)

                            binding.bottomSheetBG.visibility = View.GONE
                            hideKeyBoard(this@ImageActivity)

                            imageAdaptor?.setEditMode(false)
                            isSelection = false

                            //compressImage.visibility = View.GONE
                            binding.bottomLayout.visibility = View.GONE
                            for (n in galleryList.indices) { // For all list unchecked
                                galleryList[n].isChecked = false
                            }

                        }
                        binding.includeBottomSheetAll.btnCancel.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                        }

                        binding.bottomSheetBG.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSheetBG.visibility = View.GONE
                        }*/
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

       // getZipFiles()

     /*   binding.recycleViewCompress.setHasFixedSize(true)
        binding.recycleViewCompress.layoutManager = LinearLayoutManager(this)
        compressAdaptor = CompressedAdaptor(this, compressList)
        binding.recycleViewCompress.adapter = compressAdaptor*/

        binding.deCompressImage.setOnClickListener {
            //unZipList()
            selectedGallList = compressAdaptor!!.getAllSelected()
            for (i in selectedGallList) {
                 unzipFil(i.path, "/sdcard/myUnZipFolder")
            }
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
        }

        zipLongClick = {
            compressAdaptor?.setEditMode(true)
            compressAdaptor?.setChecked(it)
            isSelection = true

            binding.deCompressImage.visibility = View.VISIBLE

        }

       zipItemClick = {
            //getPosition = it
            if (isSelection){
                compressAdaptor?.setChecked(it)
                selectedGallList = compressAdaptor!!.getAllSelected()
                Toast.makeText(this, "Item Size are : ${selectedGallList.size}", Toast.LENGTH_SHORT).show()

                if (selectedGallList.isEmpty()){
                    compressAdaptor?.setEditMode(false)
                    isSelection = false

                    //compressImage.visibility = View.GONE
                    for (n in compressList.indices) { // For all list unchecked
                        //compressList[n].isChecked = false
                        isSelection
                    }
                }
            }
        }
    }

    private fun openBottomSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bottomSheetBGZip.visibility = View.VISIBLE

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.includeBottomSheetZip.idTxtVu.text = "Convert To UnZip"
                    binding.bottomSheetBGZip.visibility = View.VISIBLE
                    binding.bottomLayoutZip.visibility = View.GONE
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bottomSheetBGZip.visibility = View.GONE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }

    private fun getZipFiles(){
        val uriCompress = MediaStore.Files.getContentUri("external")
        val arrayProject = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATA
        )
        val selected = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
        val selectedArgs = arrayOf("application/zip")
        val sorting = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        val cursor = this.contentResolver?.query(uriCompress, arrayProject, selected, selectedArgs, sorting)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED))
                val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                val data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                compressList.add(CompressDataClass(id,title, size,uriCompress,data))
                // Use the retrieved data as required
                Log.d("TAG", "Id: $id, Name: $title, Size: $size, Date Added: $date, Mime Type: $mimeType, Data: $data")
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }

    private fun unZipList() {
        val zipFiles = compressAdaptor?.getAllSelected()
        val fileList: MutableList<File> = mutableListOf()

        for (i in zipFiles!!) {
            val f = File(i.path)

            if (f.exists()) {
                fileList.add(f)

            }
        }
        val destinationPath=  "/storage/emulated/0/DCIM/UnZipFiles"
        unzipFiles(fileList[0].path,destinationPath)

        //val targetPath = File("/storage/emulated/0/myZip.zip")
        // val targetPath = File("/storage/emulated/0/DCIM/myZip.zip")

    }
    private fun unzipFiles(zipFilePath: String, destDir: String) {
        val bufferSize = 2048
        val buffer = ByteArray(bufferSize)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            val zipInputStream = ZipInputStream(BufferedInputStream(FileInputStream(zipFilePath)))
            var zipEntry: ZipEntry? = zipInputStream.nextEntry
            while (zipEntry != null) {
                val outputFile = File(destDir, zipEntry.name)
                if (zipEntry.isDirectory) {
                    outputFile.mkdirs()
                } else {
                    val outputStream = FileOutputStream(outputFile)
                    val bufferedOutputStream = BufferedOutputStream(outputStream, bufferSize)
                    var count = zipInputStream.read(buffer, 0, bufferSize)
                    while (count != -1) {
                        bufferedOutputStream.write(buffer, 0, count)
                        count = zipInputStream.read(buffer, 0, bufferSize)
                    }
                    bufferedOutputStream.flush()
                    bufferedOutputStream.close()
                }
                zipEntry = zipInputStream.nextEntry
            }
            zipInputStream.close()
        }
    }

        fun unzipFil(zipFilePath: String, destinationFolder: String) {
            val bufferSize = 4096
            val buffer = ByteArray(bufferSize)

            // Create a ZipInputStream to read the contents of the zip file
            val zipInputStream = ZipInputStream(BufferedInputStream(FileInputStream(zipFilePath)))

            // Iterate over each entry in the zip file and extract it to the destination folder
            var zipEntry: ZipEntry? = zipInputStream.nextEntry
            while (zipEntry != null) {
                // Get the name of the entry
                val entryName = zipEntry.name

                // Create a new file in the destination folder with the same name as the entry
                val outputFile = File(destinationFolder, entryName)

                // If the entry is a directory, create it in the destination folder
                if (zipEntry.isDirectory) {
                    outputFile.mkdirs()
                } else {
                    // If the entry is a file, extract it to the destination folder
                    val parent = File(outputFile.parent)
                    if (!parent.exists()) {
                        parent.mkdirs()
                    }
                    val fileOutputStream = FileOutputStream(outputFile)
                    var count: Int
                    while (zipInputStream.read(buffer, 0, bufferSize).also { count = it } != -1) {
                        fileOutputStream.write(buffer, 0, count)
                    }
                    fileOutputStream.close()
                }
                zipEntry = zipInputStream.nextEntry
            }
            // Close the ZipInputStream
            zipInputStream.close()
        }


}