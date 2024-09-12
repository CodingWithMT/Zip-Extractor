package com.example.zipfilemanger.compressFragments

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zipfilemanger.videosFragment.AllVidFragment
import com.example.zipfilemanger.videosFragment.AviVidFragment
import com.example.zipfilemanger.videosFragment.MovVidFragment
import com.example.zipfilemanger.videosFragment.Mp4VidFragment

class ViewPagerCompressAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                ZipFragment() }
            1 -> {
                SevenZFragment() }
            2 -> {
                TarFragment() }
            3 -> {
                RarFragment() }
            else -> {throw Resources.NotFoundException("Position Not Found")}
        }
    }
}