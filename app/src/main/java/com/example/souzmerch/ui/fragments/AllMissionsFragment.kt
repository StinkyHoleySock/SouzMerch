package com.example.souzmerch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souzmerch.data.model.Mission
import com.example.souzmerch.databinding.FragmentMissionsToVerificationBinding
import com.example.souzmerch.shared.extensions.applyVisibility
import com.example.souzmerch.ui.fragments.BaseFragment
import com.example.souzmerch.ui.adapters.MissionsAdapter
import com.example.souzmerch.ui.fragments.allMissionsFragment.AllMissionsFragmentDirections
import com.example.souzmerch.ui.viewModels.MissionsViewModel

class AllMissionsFragment :
    BaseFragment<FragmentMissionsToVerificationBinding>(FragmentMissionsToVerificationBinding::inflate) {

    private lateinit var missionsViewModel: MissionsViewModel

    private val missionsAdapter by lazy {
        MissionsAdapter() {
            navigateToMissionDetails(it)
        }
    }

    private fun navigateToMissionDetails(it: Mission) {
        val action =
            AllMissionsFragmentDirections.actionAllMissionsFragmentToApproveMissionFragment(it.id)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        missionsViewModel = ViewModelProvider(this)[MissionsViewModel::class.java]

        missionsViewModel.getUnverifiedMissions()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = missionsAdapter
        }

        missionsViewModel.missionList.observe(viewLifecycleOwner) {
            missionsAdapter.setData(it)
        }

        missionsViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.applyVisibility(it)
        }
    }
}