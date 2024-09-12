package com.example.zipfilemanger.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zipfilemanger.adaptor.InternalFileAdapter
import com.example.zipfilemanger.databinding.ActivityInternalBinding
import java.io.File

class InternalActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInternalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInternalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backInternal.setOnClickListener {
            onBackPressed()
        }

        val path = Environment.getExternalStorageDirectory().path
        val root = File(path)
        val filesAndFolders = root.listFiles()

           binding.recyclerViewInternal.layoutManager = LinearLayoutManager(this)
           binding.recyclerViewInternal.adapter = InternalFileAdapter(this.applicationContext, filesAndFolders)

    }
}