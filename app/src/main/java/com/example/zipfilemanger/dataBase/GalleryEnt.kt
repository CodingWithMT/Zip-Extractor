package com.example.zipfilemanger.dataBase

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "gallery_Table")
data class GalleryEnt(
    var fName: String,
    var fPath: String,
    var fSize: String,
    var id :Int,
    var type : String
){
    @PrimaryKey(autoGenerate = true)
    var idG = 0

    @Ignore
    var isChecked = false
}
