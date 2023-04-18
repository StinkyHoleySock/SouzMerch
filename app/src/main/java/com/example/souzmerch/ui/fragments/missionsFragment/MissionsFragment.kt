package com.example.souzmerch.ui.fragments.missionsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souzmerch.data.model.Mission
import com.example.souzmerch.databinding.FragmentMyMissonsBinding
import com.example.souzmerch.shared.extensions.applyVisibility
import com.example.souzmerch.ui.fragments.BaseFragment

class MissionsFragment : BaseFragment<FragmentMyMissonsBinding>(FragmentMyMissonsBinding::inflate) {

    private lateinit var missionsViewModel: MissionsViewModel
    private val args: MissionsFragmentArgs by navArgs()
    private val missionsAdapter by lazy {
        MissionsAdapter() {
            navigateToMissionDetails(it)
        }
    }

    private fun navigateToMissionDetails(it: Mission) {
        val action = MissionsFragmentDirections.actionMissionsFragmentToMissionDetailsFragment(it.id)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        missionsViewModel = ViewModelProvider(this)[MissionsViewModel::class.java]

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