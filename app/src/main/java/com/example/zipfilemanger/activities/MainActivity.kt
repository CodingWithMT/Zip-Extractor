package com.example.zipfilemanger.activities

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.code4rox.medialoaderx.AudioLoaderX
import com.code4rox.medialoaderx.ImageLoaderX
import com.code4rox.medialoaderx.VideoLoaderX
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.example.zipfilemanger.dataBase.ViewModelGallery
import com.example.zipfilemanger.databinding.ActivityMainBinding
import com.example.zipfilemanger.models.calculatefileSize
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewVModelG: ViewModelGallery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewVModelG = ViewModelProvider(this@MainActivity)[ViewModelGallery::class.java]

        val totalStorage = getTotalInternalStorage()
        val fillStorage = getFilledInternalStorage()

        val totalGb = calculatefileSize(totalStorage)
        val fillGb = calculatefileSize(fillStorage)

        val progress = (fillStorage.toDouble() / totalStorage.toDouble() * 100).toInt()


        binding.circularProgress.progress = progress
        binding.txtVProgress.text ="$progress%"

        val text = "<font color=#DDD>$fillGb </font> <font color=#DDD>/ $totalGb </font>"
        binding.totalInternalStorage.text = Html.fromHtml(text)

        checkPermission()
        requestPermission()

        insertImage()
        insertVideos()
        insertAudio()
        //insertApks()

        binding.imageLayout.setOnClickListener {
            val intent = Intent(this, ImageActivity::class.java)
            startActivity(intent)
        }
        binding.videoLayout.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            startActivity(intent)
        }
        binding.audioLayout.setOnClickListener {
            val intent = Intent(this, AudioActivity::class.java)
            startActivity(intent)
        }
        binding.compressLayout.setOnClickListener {
            val intent = Intent(this, CompressActivity::class.java)
            startActivity(intent)
        }
        binding.docuLayout.setOnClickListener {
            val intent = Intent(this, DocumentActivity::class.java)
            startActivity(intent)
        }
        binding.appsLayout.setOnClickListener {
            val intent = Intent(this, ApkActivity::class.java)
            startActivity(intent)
        }
        binding.internalStore.setOnClickListener {
            val intent = Intent(this, InternalActivity::class.java)
            startActivity(intent)
        }

        val path = Environment.getExternalStorageDirectory().path
        val root = File(path)
        val filesAndFolders = root.listFiles()

        /*   binding.recyclerViewInternal.layoutManager = LinearLayoutManager(this)
           binding.recyclerViewInternal.adapter = InternalFileAdapter(this.applicationContext, filesAndFolders)*/
    }

    private fun checkPermission(): Boolean {

        val result =
            ContextCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {

        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        } else {
            //below android 11=======
            //startActivity(Intent(this, ActivityPicture::class.java))
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 100)
        }


        if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
        ) {
            Toast.makeText(
                this,
                "Storage permission is requires,Please allow from Settings",
                Toast.LENGTH_SHORT
            ).show()
        } else ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 111)
    }


    private fun insertImage() {
        ImageLoaderX(this).getAllImages({
            //  all images list
            for (i in 0 until it.size) {
                val name: String = it[i].name
                val nameSize: String = it[i].size.toString()
                val path: String = it[i].path
                val id: String = it[i].imageId.toString()

                val folder = GalleryEnt(name, path,nameSize, id.toInt(), "image")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
                Log.e("insertImage", "pathSize $path")
            }

            //For jpg
            val filterJpg = it.filter {
                it.name.contains(".jpg")
            }
            for (i in filterJpg.indices) {
                val name: String = filterJpg[i].name
                val path: String = filterJpg[i].path
                val id: String = filterJpg[i].imageId.toString()
                val size: String = filterJpg[i].size.toString()

                val folder = GalleryEnt(name,path, size, id.toInt(), "jpg")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
            }

            //For tiff
            val filterTiff = it.filter {
                it.name.contains(".tiff")
            }
            for (i in filterTiff.indices) {
                val name: String = filterTiff[i].name
                val path: String = filterTiff[i].path
                val id: String = filterTiff[i].imageId.toString()
                val size: String = filterTiff[i].size.toString()

                val folder = GalleryEnt(name,path, size, id.toInt(), "tiff")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
            }

            //For png
            val filterPng = it.filter {
                it.name.contains(".png")
            }
            for (i in filterPng.indices) {
                val name: String = filterPng[i].name
                val path: String = filterPng[i].path
                val id: String = filterPng[i].imageId.toString()
                val size: String = filterPng[i].size.toString()

                val folder = GalleryEnt(name,path, size, id.toInt(), "png")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
            }

            //For png
            val filterGif = it.filter {
                it.name.contains(".gif")
            }
            for (i in filterGif.indices) {
                val name: String = filterGif[i].name
                val path: String = filterGif[i].path
                val id: String = filterGif[i].imageId.toString()
                val size: String = filterGif[i].size.toString()

                val folder = GalleryEnt(name,path, size, id.toInt(), "gif")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
            }

        }, { imageFolders ->

        })
    }

    private fun insertVideos() {
        VideoLoaderX(this).getAllVideos({

            for (i in 0 until it.size) {
                val name: String = it[i].name
                val path: String = it[i].path
                val size: String = it[i].size.toString()
                val id: String = it[i].videoId.toString()
                val folder = GalleryEnt(name,path,size, id.toInt(), "video")
                viewVModelG.insertGallery(folder)
            }

            //For mp4
            val filterMp4 = it.filter {
                it.name.contains(".mp4")
            }
            for (i in filterMp4.indices) {
                val name: String = filterMp4[i].name
                val path: String = filterMp4[i].path
                val id: String = filterMp4[i].videoId.toString()
                val size: String = filterMp4[i].size.toString()

                val folder = GalleryEnt(name,path, size, id.toInt(), "mp4")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
            }

            //For mov
            val filterMov = it.filter {
                it.name.contains(".mov")
            }
            for (i in filterMov.indices) {
                val name: String = filterMov[i].name
                val path: String = filterMov[i].path
                val id: String = filterMov[i].videoId.toString()
                val size: String = filterMov[i].size.toString()

                val folder = GalleryEnt(name,path, size, id.toInt(), "mov")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
            }

            //For avi
            val filterAvi = it.filter {
                it.name.contains(".avi")
            }
            for (i in filterAvi.indices) {
                val name: String = filterAvi[i].name
                val path: String = filterAvi[i].path
                val id: String = filterAvi[i].videoId.toString()
                val size: String = filterAvi[i].size.toString()

                val folder = GalleryEnt(name,path, size, id.toInt(), "avi")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
            }

        }, { videoFolders ->

        })
    }

    fun getTotalInternalStorage(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return blockSize * totalBlocks
    }
    // function to get filled internal storage
    fun getFilledInternalStorage(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong
        return blockSize * (totalBlocks - availableBlocks)
    }

    private fun insertAudio() {
        AudioLoaderX(this).getAllAudios({

            for (i in 0 until it.size) {
                val name: String = it[i].name
                val path: String = it[i].path
                val size: String = it[i].size.toString()
                val id: String = it[i].audioId.toString()
                val folder = GalleryEnt(name, path,size, id.toInt(), "audio")
                viewVModelG.insertGallery(folder)
            }

            //For avi
            val filterMp3 = it.filter {
                it.name.contains(".mp3")
            }
            for (i in filterMp3.indices) {
                val name: String = filterMp3[i].name
                val path: String = filterMp3[i].path
                val id: String = filterMp3[i].audioId.toString()
                val size: String = filterMp3[i].size.toString()

                val folder = GalleryEnt(name,path, size, id.toInt(), "mp3")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
            }

            //For avi
            val filterAudMp4 = it.filter {
                it.name.contains(".mp4")
            }
            for (i in filterAudMp4.indices) {
                val name: String = filterAudMp4[i].name
                val path: String = filterAudMp4[i].path
                val id: String = filterAudMp4[i].audioId.toString()
                val size: String = filterAudMp4[i].size.toString()

                val folder = GalleryEnt(name,path, size, id.toInt(), "mp4_Aud")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
            }

            //For avi
            val filterAac = it.filter {
                it.name.contains(".aac")
            }
            for (i in filterAac.indices) {
                val name: String = filterAac[i].name
                val path: String = filterAac[i].path
                val id: String = filterAac[i].audioId.toString()
                val size: String = filterAac[i].size.toString()

                val folder = GalleryEnt(name,path, size, id.toInt(), "aac")
                // galleryList.add(folder)
                viewVModelG.insertGallery(folder)
            }

        }, { audioFolders ->

        })
    }


    private fun insertApks() {

        //val appsPath = Environment.getExternalStorageDirectory().toString() + "/Android/data"
        //val appsDirectory = File(appsPath)
        //val appsList = appsDirectory.listFiles()

        val packageManager = this.packageManager
        val installedApps = packageManager?.getInstalledApplications(PackageManager.GET_META_DATA)
        //val appNames = mutableListOf<String>()
        //val appIds = mutableListOf<String>()

        for (app in installedApps!!) {
            val appName = packageManager.getApplicationLabel(app).toString()
            //val appId = app.packageName
            val name = app.name
            val size = app.sourceDir

            //apkFiles.get(0).path
            //  appNames.add(appName)
//            appIds.add(appId)

            val folder = GalleryEnt(name!!, "size","", 0, "apps")

            viewVModelG.insertGallery(folder)
        }
    }

}