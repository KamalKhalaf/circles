package com.circles.circlesapp.phase2.views.ui.profile


import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.circles.circlesapp.R
import com.circles.circlesapp.databinding.LayoutEdtProfileSocialMediaBinding
import com.circles.circlesapp.databinding.PrivacyLayout2Binding
import com.circles.circlesapp.databinding.SettingsLayoutBinding
import com.circles.circlesapp.isVisible
import com.circles.circlesapp.phase2.views.adapter.ListOfSocialMediasAdapter


/**
 * A simple [Fragment] subclass.
 *
 */
class SocialMediaFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val list = listOf(SocialMediaModel("https://Facebookurl", R.drawable.ic_social_face, R.drawable.ic_social_media_black),
                SocialMediaModel("https://Instaurl", R.drawable.ic_social_insta, 0),
                SocialMediaModel("https://Snapurl", R.drawable.ic_social_snap, 0),
                SocialMediaModel("https://Twitterurl", R.drawable.ic_social_twitter, 0)
        )
        val binding = DataBindingUtil.inflate<LayoutEdtProfileSocialMediaBinding>(layoutInflater, R.layout.layout_edt_profile_social_media, container, false)
        val viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel::class.java)
        val mediasAdapter = ListOfSocialMediasAdapter(list)
        binding.rvSocialLinks.layoutManager=LinearLayoutManager(activity)
        binding.rvSocialLinks.adapter=mediasAdapter
        return binding.root
    }


}
