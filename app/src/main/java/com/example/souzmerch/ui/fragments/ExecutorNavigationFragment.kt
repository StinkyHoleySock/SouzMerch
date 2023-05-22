package com.example.souzmerch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.souzmerch.R
import com.example.souzmerch.databinding.FragmentExecutorNavigationBinding
import com.example.souzmerch.ui.BaseFragment

class ExecutorNavigationFragment :
    BaseFragment<FragmentExecutorNavigationBinding>(FragmentExecutorNavigationBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMyShops.setOnClickListener {
            findNavController().navigate(R.id.action_executorNavigationFragment_to_shopsFragment)
        }

        binding.btnTasks.setOnClickListener {
            findNavController().navigate(R.id.action_executorNavigationFragment_to_allMissionsFragment)
        }
    }

}