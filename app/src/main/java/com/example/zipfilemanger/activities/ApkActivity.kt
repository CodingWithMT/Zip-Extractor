package com.example.zipfilemanger.activities

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.R
import com.example.zipfilemanger.adaptor.*
import com.example.zipfilemanger.databinding.ActivityApkBinding
import com.example.zipfilemanger.models.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ApkActivity : AppCompatActivity() {
    private lateinit var binding : ActivityApkBinding

    private var listApps : ArrayList<CompressDataClass> = ArrayList()
    private var adaptorApps: AppsAdaptor? = null

    private var selectedAppsList: ArrayList<CompressDataClass> = ArrayList()
    private var isSelection = false
    var fileName = ""

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backApkAct.setOnClickListener {
            onBackPressed()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.includeBottomSheetApps.bottomSheet)
        this.bottomSheetPeek(action = {
            bottomSheetBehavior.peekHeight = 0
            /* if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                 binding.root.setBackgroundColor(Color.BLACK)
             }*/
        }, R.layout.activity_apk, 0)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        getApkFiles()

        binding.recycleViewApk.setHasFixedSize(true)
        binding.recycleViewApk.layoutManager = LinearLayoutManager(this)
        adaptorApps = AppsAdaptor(this, listApps)
        binding.recycleViewApk.adapter = adaptorApps

        if (listApps.isEmpty()){
            binding.noApkLayout.visibility = View.VISIBLE
        }

        appsLongClick = {
            adaptorApps?.setEditMode(true)
            adaptorApps?.setChecked(it)
            isSelection = true

            selectedAppsList = adaptorApps!!.getAllSelected()
            binding.textCountApps.text = "Items ${selectedAppsList.size}"

            binding.bottomLayoutApps.visibility = View.VISIBLE
        }

        appsItemClick = {
            if (isSelection){
                adaptorApps?.setChecked(it)

                selectedAppsList = adaptorApps!!.getAllSelected()
                binding.textCountApps.text = "Items ${selectedAppsList.size}"
            }
        }

        binding.compressedLayoutApps.setOnClickListener {
            openBottomSheet()
        }

        binding.includeBottomSheetApps.btnOk.setOnClickListener {

            fileName = binding.includeBottomSheetApps.filenameInput.text.toString()
            zipList(fileName)

            for (n in listApps.indices) {
                listApps[n].isChecked = false
            }
            isSelection = false
            adaptorApps?.setEditMode(false)
            adaptorApps?.notifyDataSetChanged()

            hideKeyBoard(this@ApkActivity)

            // if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            //  }
            binding.bottomSheetBGApps.visibility = View.GONE
            binding.bottomLayoutApps.visibility = View.GONE

        }
        binding.includeBottomSheetApps.btnCancel.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.bottomSheetBGApps.visibility = View.GONE

            if (isSelection){
                for (n in listApps.indices) {
                    listApps[n].isChecked = false
                }
                isSelection = false
                adaptorApps?.setEditMode(false)
            }
            adaptorApps?.notifyDataSetChanged()

            binding.bottomLayoutApps.visibility = View.GONE
        }

        binding.bottomSheetBGApps.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.bottomSheetBGApps.visibility = View.GONE
        }

    }

    private fun getApkFiles(){
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
        val selectedArgs = arrayOf("application/vnd.android.package-archive")
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
                listApps.add(CompressDataClass(id,title, size,uriCompress,data))
                // Use the retrieved data as required
                Log.d("TAG", "Id: $id, Name: $title, Size: $size, Date Added: $date, Mime Type: $mimeType, Data: $data")
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }

    private fun zipList(name : String){
        val zipFiles = adaptorApps?.getAllSelected()
        val fileList: MutableList<File> = mutableListOf()
        for (i in zipFiles!!) {
            val f = File(i.path)
            if (f.exists()) {
                fileList.add(f)
            }
        }
        val filePath = File("/storage/emulated/0/$name.zip")

        //val filePath = File(imagePath)
        zipFiles(fileList, filePath)
        Toast.makeText(this, "This is path : $filePath", Toast.LENGTH_SHORT).show()
    }
    private fun zipFiles(files: List<File>, targetFile: File) {
        val buffer = ByteArray(1024)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            val fos = FileOutputStream(targetFile)
            val bos = BufferedOutputStream(fos)
            val zos = ZipOutputStream(bos)
            // loop through each file and add it to the zip file
            for (file in files) {
                val fis = FileInputStream(file)
                val bis = BufferedInputStream(fis)
                val entry = ZipEntry(file.name)
                zos.putNextEntry(entry)
                // write the file contents to the zip file
                var len: Int
                while (bis.read(buffer).also { len = it } > 0) {
                    zos.write(buffer, 0, len)
                }
                bis.close()
                zos.closeEntry()
            }
            zos.close()
        }
    }

    private fun openBottomSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bottomSheetBGApps.visibility = View.VISIBLE

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //binding.includeBottomSheetDocu.idTxtVu.text = "Convert To Zip"
                    binding.bottomSheetBGApps.visibility = View.VISIBLE
                    binding.bottomLayoutApps.visibility = View.GONE
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bottomSheetBGApps.visibility = View.GONE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }
}