package com.benjaminwan.epoxyswipedemo.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.benjaminwan.epoxyswipedemo.R
import com.benjaminwan.epoxyswipedemo.databinding.FragmentMainBinding
import com.benjaminwan.epoxyswipedemo.utils.viewBinding

class MainFragment(@LayoutRes contentLayoutId: Int = R.layout.fragment_main) :
    Fragment(contentLayoutId) {

    private val binding: FragmentMainBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.simpleBtn.setOnClickListener {
            findNavController().navigate(R.id.simpleFragment)
        }
        binding.recyclerViewBtn.setOnClickListener {
            findNavController().navigate(R.id.recyclerViewFragment)
        }
        binding.recyclerViewTypesBtn.setOnClickListener {
            findNavController().navigate(R.id.recyclerViewTypesFragment)
        }

    }

}