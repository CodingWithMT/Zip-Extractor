package com.example.zipfilemanger.imageFragment.adaptor

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zipfilemanger.imageFragment.*

class ViewPagerImageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                AllImageFragment() }
            1 -> {
                JpgImgFragment() }
            2 -> {
                TiffImgFragment() }
            3 -> {
                PngImgFragment() }
            4 -> {
                GifImgFragment() }

            else -> {throw Resources.NotFoundException("Position Not Found")}
        }
    }
}