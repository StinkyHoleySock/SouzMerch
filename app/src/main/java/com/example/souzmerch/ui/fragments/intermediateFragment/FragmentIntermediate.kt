package com.example.souzmerch.ui.fragments.intermediateFragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.souzmerch.R
import com.example.souzmerch.databinding.FragmentIntermediateBinding
import com.example.souzmerch.ui.fragments.BaseFragment

class FragmentIntermediate :
    BaseFragment<FragmentIntermediateBinding>(FragmentIntermediateBinding::inflate) {

    private val args: FragmentIntermediateArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.customer) findNavController().navigate(R.id.action_fragmentIntermediate_to_customerNavigationFragment)
        if (args.executor) findNavController().navigate(R.id.action_fragmentIntermediate_to_executorNavigationFragment)
        if (args.merch) findNavController().navigate(R.id.action_fragmentIntermediate_to_merchNavigationFragment)

        binding.progressCircular.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentIntermediate_to_merchNavigationFragment)
        }
    }
}