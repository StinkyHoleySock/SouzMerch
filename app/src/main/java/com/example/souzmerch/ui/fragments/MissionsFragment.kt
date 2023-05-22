package com.example.souzmerch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souzmerch.data.model.Mission
import com.example.souzmerch.databinding.FragmentMyMissonsBinding
import com.example.souzmerch.shared.extensions.applyVisibility
import com.example.souzmerch.ui.BaseFragment
import com.example.souzmerch.ui.adapters.MissionsAdapter
import com.example.souzmerch.ui.viewModels.MissionsViewModel
import com.example.souzmerch.ui.viewModels.ShopsViewModel

class MissionsFragment : BaseFragment<FragmentMyMissonsBinding>(FragmentMyMissonsBinding::inflate) {

    private lateinit var missionsViewModel: MissionsViewModel
    private lateinit var shopsViewModel: ShopsViewModel
    private val args: MissionsFragmentArgs by navArgs()
    private val missionsAdapter by lazy {
        MissionsAdapter() {
            navigateToMissionDetails(it)
        }
    }

    private fun navigateToMissionDetails(it: Mission) {
        val action =
            MissionsFragmentDirections.actionMissionsFragmentToMissionDetailsFragment(
                it.id,
                args.shopId
            )
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        missionsViewModel = ViewModelProvider(this)[MissionsViewModel::class.java]
        shopsViewModel = ViewModelProvider(this)[ShopsViewModel::class.java]

        missionsViewModel.getMissions(args.shopId)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = missionsAdapter
        }

        missionsViewModel.missionList.observe(viewLifecycleOwner) { missions ->
            missionsAdapter.setData(missions)
        }

        missionsViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.applyVisibility(it)
        }
    }
}