package com.example.souzmerch.ui.fragments.approveMissionFragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.souzmerch.data.enums.MissionState
import com.example.souzmerch.databinding.FragmentApproveMissionBinding
import com.example.souzmerch.ui.fragments.BaseFragment
import com.example.souzmerch.ui.fragments.merchNavigationFragment.ShopsViewModel
import com.example.souzmerch.ui.fragments.missionDetailsFragment.MissionDetailsViewModel

class ApproveMissionFragment :
    BaseFragment<FragmentApproveMissionBinding>(FragmentApproveMissionBinding::inflate) {

    private val args: ApproveMissionFragmentArgs by navArgs()

    private lateinit var missionDetailsViewModel: MissionDetailsViewModel
    private lateinit var shopsViewModel: ShopsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        missionDetailsViewModel = ViewModelProvider(this)[MissionDetailsViewModel::class.java]
        shopsViewModel = ViewModelProvider(this)[ShopsViewModel::class.java]

        missionDetailsViewModel.getMission(args.shopId)
        shopsViewModel.getShopById(args.shopId)

        shopsViewModel.shop.observe(viewLifecycleOwner) {
            with(binding) {
                tvTitle.text = it.name
            }
        }

        missionDetailsViewModel.mission.observe(viewLifecycleOwner) {
            with(binding) {
                etTask.setText(it.product)
                etAmount.setText(it.amount.toString())
                etComment.setText(it.comment)
            }
        }

        binding.btnApprove.setOnClickListener {
            missionDetailsViewModel.updateMissionStatus(args.shopId, MissionState.COMPLETE.name)
            findNavController().popBackStack()
        }

        binding.btnComment.setOnClickListener {
            missionDetailsViewModel.updateMissionStatus(args.shopId, MissionState.PROGRESS.name)
            missionDetailsViewModel.updateMissionComment(
                args.shopId,
                binding.etComment.text.toString()
            )
            findNavController().popBackStack()
        }
    }
}