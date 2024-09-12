package com.example.zipfilemanger.models

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.zipfilemanger.dataBase.GalleryEnt
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

const val imagePath = "/storage/emulated/0/compressedImage.zip"

fun calculatefileSize(size: Long): kotlin.String {
    val byteUnits = listOf("B", "KB", "MB", "GB", "TB")
    var fileSize = size.toDouble()
    var index = 0
    while (fileSize >= 1024 && index < byteUnits.size - 1) {
        fileSize /= 1024
        index++
    }
    return "%.2f %s".format(fileSize, byteUnits[index])
}

fun hideKeyBoard(activity: Activity) {
    val imm: InputMethodManager =
        activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.bottomSheetPeek(
    action: BottomSheetDialog.() -> Unit,
    @LayoutRes layout: Int,
    peekHeight: Int
) {
    BottomSheetDialog(this).apply {
        behavior.peekHeight = peekHeight
        action.invoke(this)
        val view = layoutInflater.inflate(layout, null)
        //setCancelable(false)
        setContentView(view)
        //show()
    }
}

fun compressInZip(name : String, list : ArrayList<GalleryEnt>,context: Context){

    //val zipFiles = imageAdaptor?.getAllSelected()
    val fileList: MutableList<File> = mutableListOf()
    for (i in list) {
        val f = File(i.fPath)
        if (f.exists()) {
            fileList.add(f)
        }
    }
    val filePath = File("/storage/emulated/0/$name.zip")

    //val filePath = File(imagePath)
    zipFiles(fileList, filePath,context)
    Toast.makeText(context, "This is path : $filePath", Toast.LENGTH_SHORT).show()
}

private fun zipFiles(files: List<File>, targetFile: File,context: Context) {
    val buffer = ByteArray(1024)
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
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

fun compressIn7Z(nameFol : String,list :ArrayList<GalleryEnt>,context: Context){
    //val seven7Files = imageAdaptor?.getAllSelected()
    val file7zList: MutableList<File> = mutableListOf()
    for (i in list) {
        val f = File(i.fPath)
        if (f.exists()) {
            file7zList.add(f)
        }
    }
    //al inputFilePaths = arrayListOf("/path/to/image1.jpg", "/path/to/image2.jpg")
    val outputFilePath = File("/storage/emulated/0/$nameFol.7z")
    compressImagesTo7z(file7zList,outputFilePath)
    ////////// compressImages7z(file7zList, outputFilePath)
    Toast.makeText(context, "This is path : $outputFilePath", Toast.LENGTH_SHORT).show()
}

private fun compressImagesTo7z(inputFilePaths: List<File>, outputFilePath: File) {
    val outputFileStream = FileOutputStream(outputFilePath)
    try {
        //val outputFile = File(outputFilePath)
        //val bos = BufferedOutputStream(outputFileStream)
        val sevenZOutput = SevenZOutputFile(outputFilePath)

        for (inputFilePath in inputFilePaths) {
            //val inputFile = File(inputFilePath)
            val inputFileStream = FileInputStream(inputFilePath)
            val entry = SevenZArchiveEntry()
            entry.name = inputFilePath.name
            sevenZOutput.putArchiveEntry(entry)

            val bytes = ByteArray(6554)
            var count = inputFileStream.read(bytes)
            while (count != -1) {
                //sevenZOutput.write(bytes, 0, count)
                count = inputFileStream.read(bytes)
            }

            sevenZOutput.closeArchiveEntry()
            inputFileStream.close()
        }

        sevenZOutput.close()
        outputFileStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }finally {
        outputFileStream.close()
    }
}

var cancelBottomSheet: (() -> Unit)? = null
var btnOkBottomSheetAll: ((String) -> Unit)? = null

var btnOkBottomSheetZip: (() -> Unit)? = null
var cancelBottomSheetZip: (() -> Unit)? = null

var btnOkBottomSheetVid: (() -> Unit)? = null
var cancelBottomSheetVid: (() -> Unit)? = null

var  btnOkBottomSheetAud: (() -> Unit)? = null
var cancelBottomSheetAud: (() -> Unit)? = null

var  btnOkBottomSheetDocu: (() -> Unit)? = null
var cancelBottomSheetDocu: (() -> Unit)? = null