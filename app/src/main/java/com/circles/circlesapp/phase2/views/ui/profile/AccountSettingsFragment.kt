package com.circles.circlesapp.phase2.views.ui.profile

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.circles.circlesapp.R
import com.circles.circlesapp.databinding.SettingsLayoutBinding
import com.circles.circlesapp.isVisible

class AccountSettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<SettingsLayoutBinding>(layoutInflater, R.layout.settings_layout, container, false)
        val viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel::class.java)

        binding.changeLan.setOnClickListener {
            if (binding.llLanguagesRadios.isVisible()) {
                binding.llLanguagesRadios.visibility = View.GONE
            } else {
                binding.llLanguagesRadios.visibility = View.VISIBLE
            }
        }
        binding.clContactUs.setOnClickListener {
            if (binding.clContactForm.isVisible()) {
                binding.clContactForm.visibility = View.GONE
            } else {
                binding.clContactForm.visibility = View.VISIBLE
            }
        }
        binding.privacy.setOnClickListener {
            showFragment(PrivacySettingsFragment())
        }
        binding.clTermsConditions.setOnClickListener {
            showFragment(TermsAndConditionsFragment())
        }
        binding.logout.setOnClickListener {
           activity?.finish()
        }

        binding.socialMedia.setOnClickListener {
            showFragment(SocialMediaFragment())
        }

        return binding.root
    }

    private fun showInnerFragment(fragment: Fragment) {
        activity!!.supportFragmentManager
                .beginTransaction()
                .add(R.id.content, fragment, "")
                .addToBackStack(null)
                .commit()
    }
    private fun showFragment(fragment: Fragment) {
        activity!!.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "rageComicList")
                .addToBackStack(null)
                .commit()
    }
}
