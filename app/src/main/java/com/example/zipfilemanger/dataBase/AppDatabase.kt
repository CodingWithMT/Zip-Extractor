package com.example.zipfilemanger.dataBase

import android.content.Context
import androidx.room.*

@Database(entities = [GalleryEnt::class], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

    abstract fun dataDao(): DataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDataBase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance
                instance.getOpenHelper().getWritableDatabase()
                instance
            }
        }
    }
}