package com.example.zipfilemanger.dataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DataDao {
   // @Query("SELECT * FROM `gallery_Table` where type")
    //fun getAll(): LiveData<List<GalleryEnt>>


    @Query("SELECT * FROM gallery_Table WHERE type = :imageType")
    fun getAllImage(imageType: String) : LiveData<List<GalleryEnt>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGallery(save: GalleryEnt)
}