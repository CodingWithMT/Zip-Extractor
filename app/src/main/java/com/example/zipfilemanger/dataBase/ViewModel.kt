package com.example.zipfilemanger.dataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelGallery(application: Application) : AndroidViewModel(application) {
    private val getAllImage: LiveData<List<GalleryEnt>>
    private val getAllJpg: LiveData<List<GalleryEnt>>
    private val getAllTiff: LiveData<List<GalleryEnt>>
    private val getAllPng: LiveData<List<GalleryEnt>>
    private val getAllGif: LiveData<List<GalleryEnt>>

    private val getAllVideos: LiveData<List<GalleryEnt>>
    private val getAllMp4: LiveData<List<GalleryEnt>>
    private val getAllMov: LiveData<List<GalleryEnt>>
    private val getAllAvi: LiveData<List<GalleryEnt>>

    private val getAllAudio: LiveData<List<GalleryEnt>>
    private val getAllAudMp3: LiveData<List<GalleryEnt>>
    private val getAllAudMp4: LiveData<List<GalleryEnt>>
    private val getAllAudAac: LiveData<List<GalleryEnt>>

    private val getAllApps: LiveData<List<GalleryEnt>>


    private val repository: Repository
    init {
        val dao = AppDatabase.getDataBase(application).dataDao()
        repository = Repository(dao)

        getAllImage = repository.getAllImage
        getAllJpg = repository.getAllJpg
        getAllTiff = repository.getAllTiff
        getAllPng = repository.getAllPng
        getAllGif = repository.getAllGif
        ////////////////////////////////
        getAllVideos = repository.getAllVideos
        getAllMp4 = repository.getAllMp4
        getAllMov = repository.getAllMov
        getAllAvi = repository.getAllAvi
        //////////////////////////////////
        getAllAudio = repository.getAllAudio
        getAllAudMp3 = repository.getAllAudMp3
        getAllAudMp4 = repository.getAllAudMp4
        getAllAudAac = repository.getAllAudAac
        /////////////////////////////////////
        getAllApps = repository.getAllApps

    }
    fun getAllImage() = repository.getAllImage
    fun getAllJpg() = repository.getAllJpg
    fun getAllTiff() = repository.getAllTiff
    fun getAllPng() = repository.getAllPng
    fun getAllGif() = repository.getAllGif

    fun getAllVideos() = repository.getAllVideos
    fun getAllMp4() = repository.getAllMp4
    fun getAllMov() = repository.getAllMov
    fun getAllAvi() = repository.getAllAvi

    fun getAllAudio() = repository.getAllAudio
    fun getAllAudMp3() = repository.getAllAudMp3
    fun getAllAudMp4() = repository.getAllAudMp4
    fun getAllAudAAC() = repository.getAllAudAac

    fun getAllApps() = repository.getAllApps

    fun insertGallery(save: GalleryEnt) = CoroutineScope(Dispatchers.IO).launch  {
        repository.insertGallery(save)
    }
}