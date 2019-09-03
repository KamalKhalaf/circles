package com.circles.circlesapp.phase2.views.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.circles.circlesapp.R
import com.circles.circlesapp.databinding.SettingsLayoutBinding
import com.circles.circlesapp.isVisible

class AccountSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<SettingsLayoutBinding>(this, R.layout.settings_layout)
        binding.changeLan.setOnClickListener{
            if (binding.llLanguagesRadios.isVisible()){
                Toast.makeText(this,"vi",Toast.LENGTH_SHORT).show()
                binding.llLanguagesRadios.visibility=View.GONE
            }else{
                Toast.makeText(this,"go",Toast.LENGTH_SHORT).show()
                binding.llLanguagesRadios.visibility=View.VISIBLE

            }
        }

    }
}
