package com.example.souzmerch.ui.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.souzmerch.data.enums.MissionState
import com.example.souzmerch.databinding.FragmentApproveMissionBinding
import com.example.souzmerch.ui.BaseFragment
import com.example.souzmerch.ui.viewModels.ShopsViewModel
import com.example.souzmerch.ui.viewModels.MissionDetailsViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

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

        missionDetailsViewModel.mission.observe(viewLifecycleOwner) { mission ->
            with(binding) {
                etTask.setText(mission.product)
                etAmount.setText(mission.amount.toString())
                etComment.setText(mission.comment)

                val storageRef = mission.photo?.let { photo ->
                    Firebase.storage.reference.child("file").child(
                        photo
                    )
                }

                val localFile = File.createTempFile("tempImage", "jpg")
                storageRef?.getFile(localFile)?.addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    binding.ivReport.setImageBitmap(bitmap)
                }
                Log.d("develop", "ref: $storageRef")
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