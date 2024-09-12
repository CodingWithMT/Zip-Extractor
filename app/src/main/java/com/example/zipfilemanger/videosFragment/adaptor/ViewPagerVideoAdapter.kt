package com.example.zipfilemanger.videosFragment.adaptor

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zipfilemanger.videosFragment.AllVidFragment
import com.example.zipfilemanger.videosFragment.AviVidFragment
import com.example.zipfilemanger.videosFragment.MovVidFragment
import com.example.zipfilemanger.videosFragment.Mp4VidFragment

class ViewPagerVideoAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                AllVidFragment() }
            1 -> {
                Mp4VidFragment() }
            2 -> {
                MovVidFragment() }
            3 -> {
                AviVidFragment() }
            else -> {throw Resources.NotFoundException("Position Not Found")}
        }
    }
}