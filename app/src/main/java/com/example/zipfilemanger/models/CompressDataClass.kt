package com.example.zipfilemanger.models

import android.net.Uri
import com.example.zipfilemanger.dataBase.GalleryEnt
import java.net.IDN

data class CompressDataClass(val id: Long, val name : String, val size : Long, val uri: Uri,val path : String){
    var isChecked : Boolean = false

    companion object {
        var allImageList: ArrayList<GalleryEnt> = ArrayList()
        var selectedCount = 0
    }
}


