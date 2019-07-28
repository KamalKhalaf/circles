package com.circles.circlesapp.profile

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MyPagerAdapter(fm: FragmentManager,tokenType:String,accesToken:String) : FragmentPagerAdapter(fm) {
    val tokenType = tokenType
    val accesToken =  accesToken
    override fun getItem(position: Int): Fragment {
        var selectedFragment = Fragment()
        when (position) {
            0 -> selectedFragment = UserPostsFragment.newInstance(tokenType, accesToken)
            1 -> selectedFragment =  UserLikedPostsFragment.newInstance(tokenType, accesToken)
        }
        var sf = selectedFragment
            return selectedFragment
        }


    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "MY Posts"
            else -> {
                return "Liked Posts"
            }
        }
    }
}
