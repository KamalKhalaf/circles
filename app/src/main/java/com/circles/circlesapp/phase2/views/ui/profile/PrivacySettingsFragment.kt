package com.circles.circlesapp.phase2.views.ui.profile


import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.circles.circlesapp.R
import com.circles.circlesapp.databinding.PrivacyLayout2Binding
import com.circles.circlesapp.databinding.SettingsLayoutBinding
import com.circles.circlesapp.isVisible


/**
 * A simple [Fragment] subclass.
 *
 */
class PrivacySettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<PrivacyLayout2Binding>(layoutInflater, R.layout.privacy_layout2, container, false)
        val viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel::class.java)

        return binding.root
    }


}
