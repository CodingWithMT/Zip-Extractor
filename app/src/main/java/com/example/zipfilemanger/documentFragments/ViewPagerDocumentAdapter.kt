package com.example.zipfilemanger.documentFragments

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerDocumentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 6

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                AllDocuFragment() }
            1 -> {
                WordDocuFragment() }
            2 -> {
                ExelDocuFragment() }
            3 -> {
                PdfDocuFragment() }
            4 -> {
                PptDocuFragment() }
            5 -> {
                TxtDocuFragment() }

            else -> {throw Resources.NotFoundException("Position Not Found")}
        }
    }
}