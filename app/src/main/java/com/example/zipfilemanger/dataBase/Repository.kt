package com.example.zipfilemanger.dataBase

import androidx.lifecycle.LiveData

class Repository(private val getDao: DataDao) {

    val getAllImage: LiveData<List<GalleryEnt>> = getDao.getAllImage("image")
    val getAllJpg: LiveData<List<GalleryEnt>> = getDao.getAllImage("jpg")
    val getAllTiff: LiveData<List<GalleryEnt>> = getDao.getAllImage("tiff")
    val getAllPng: LiveData<List<GalleryEnt>> = getDao.getAllImage("png")
    val getAllGif: LiveData<List<GalleryEnt>> = getDao.getAllImage("gif")

    val getAllVideos: LiveData<List<GalleryEnt>> = getDao.getAllImage("video")
    val getAllMp4: LiveData<List<GalleryEnt>> = getDao.getAllImage("mp4")
    val getAllMov: LiveData<List<GalleryEnt>> = getDao.getAllImage("mov")
    val getAllAvi: LiveData<List<GalleryEnt>> = getDao.getAllImage("avi")

    val getAllAudio: LiveData<List<GalleryEnt>> = getDao.getAllImage("audio")
    val getAllAudMp3: LiveData<List<GalleryEnt>> = getDao.getAllImage("mp3")
    val getAllAudMp4: LiveData<List<GalleryEnt>> = getDao.getAllImage("mp4_Aud")
    val getAllAudAac: LiveData<List<GalleryEnt>> = getDao.getAllImage("aac")



    val getAllApps: LiveData<List<GalleryEnt>> = getDao.getAllImage("apps")


    suspend fun insertGallery(save: GalleryEnt) {
        getDao.insertGallery(save)
    }
}