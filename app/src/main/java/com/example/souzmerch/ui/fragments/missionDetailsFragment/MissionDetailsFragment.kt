package com.example.souzmerch.ui.fragments.missionDetailsFragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.souzmerch.data.enums.MissionState
import com.example.souzmerch.databinding.FragmentMissionDetailsBinding
import com.example.souzmerch.shared.extensions.applyVisibility
import com.example.souzmerch.shared.extensions.convertLongToTime
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
            binding.llComment.applyVisibility(mission.comment != "")

            binding.tvAmount.text = mission.amount.toString()
            binding.tvProduct.text = mission.product
            binding.tvComment.text = mission.comment

            if (mission.startTime?.isNotEmpty() == true)
                binding.tvStartTime.text = mission.startTime.toLong().convertLongToTime()

            when (mission.status) {
                MissionState.CREATE.name -> {
                    binding.btnSave.text = "Принять задачу"
                }
                MissionState.PROGRESS.name -> {
                    binding.btnSave.text = "Сохранить"
                    binding.btnSave.setBackgroundColor(Color.BLUE)
                }
                MissionState.VERIFICATION.name -> {
                    binding.btnSave.text = "На проверке"
                    binding.btnSave.isEnabled = false
                    binding.btnSave.setBackgroundColor(Color.YELLOW)
                    binding.btnSave.setTextColor(Color.BLACK)
                }
                MissionState.COMPLETE.name -> {
                    binding.btnSave.text = "Выполнено"
                    binding.btnSave.isEnabled = false
                    binding.btnSave.setBackgroundColor(Color.GREEN)
                    binding.btnSave.setTextColor(Color.BLACK)
                }
            }

        }
        binding.btnSave.setOnClickListener {
            val mission = missionDetailsViewModel.mission.value
            when (mission?.status) {
                MissionState.CREATE.name -> {
                    val startTime = System.currentTimeMillis().toString()
                    missionDetailsViewModel.updateMissionStatus(
                        args.missionId,
                        MissionState.PROGRESS.name
                    )
                    mission.id.let { id ->
                        missionDetailsViewModel.updateMissionStartTime(
                            id,
                            startTime
                        )
                    }
                    findNavController().popBackStack()
                }
                MissionState.PROGRESS.name -> {
                    missionDetailsViewModel.updateMissionStatus(
                        args.missionId,
                        MissionState.VERIFICATION.name
                    )
                    findNavController().popBackStack()
                    //todo: логика обработки фото
                }
                MissionState.VERIFICATION.name -> {}
                MissionState.COMPLETE.name -> {}

            }
        }
    }
}