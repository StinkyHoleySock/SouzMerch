package com.example.souzmerch.ui.fragments.missionDetailsFragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.souzmerch.data.enums.MissionState
import com.example.souzmerch.databinding.FragmentMissionDetailsBinding
import com.example.souzmerch.ui.fragments.BaseFragment

class MissionDetailsFragment :
    BaseFragment<FragmentMissionDetailsBinding>(FragmentMissionDetailsBinding::inflate) {

    val args: MissionDetailsFragmentArgs by navArgs()
    lateinit var missionDetailsViewModel: MissionDetailsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        missionDetailsViewModel = ViewModelProvider(this)[MissionDetailsViewModel::class.java]

        missionDetailsViewModel.getMission(args.missionId)

        missionDetailsViewModel.mission.observe(viewLifecycleOwner) { mission ->
            binding.tvAmount.text = mission.amount.toString()
            binding.tvProduct.text = mission.product
            binding.tvStartTime.text = mission.startTime

            when (mission.status) {
                MissionState.CREATE.name -> {
                    binding.btnSave.text = "Принять задачу"
                }
                MissionState.PROGRESS.name -> {
                    binding.btnSave.text = "Принять задачу"
                }
                MissionState.VERIFICATION.name -> {
                    binding.btnSave.text = "На проверке"
                }
                MissionState.COMPLETE.name -> {}
            }

            binding.btnSave.setOnClickListener {
                if (mission.status == MissionState.CREATE.name) {
                    val startTime = System.currentTimeMillis().toString()
                    missionDetailsViewModel.updateMissionStatus(mission.id, MissionState.PROGRESS.name)
                    missionDetailsViewModel.updateMissionStartTime(mission.id, startTime)
                }
            }
        }
    }
}