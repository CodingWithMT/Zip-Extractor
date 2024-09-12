package com.example.zipfilemanger.audioFragment.adaptor

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zipfilemanger.audioFragment.AacAudFragment
import com.example.zipfilemanger.audioFragment.AllAudioFragment
import com.example.zipfilemanger.audioFragment.Mp3AudFragment
import com.example.zipfilemanger.audioFragment.Mp4AudFragment
import com.example.zipfilemanger.videosFragment.AllVidFragment
import com.example.zipfilemanger.videosFragment.AviVidFragment
import com.example.zipfilemanger.videosFragment.MovVidFragment
import com.example.zipfilemanger.videosFragment.Mp4VidFragment

class ViewPagerAudioAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                AllAudioFragment() }
            1 -> {
                Mp3AudFragment() }
            2 -> {
                Mp4AudFragment() }
            3 -> {
                AacAudFragment() }
            else -> {throw Resources.NotFoundException("Position Not Found")}
        }
    }
}