package com.example.souzmerch.ui.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.souzmerch.databinding.FragmentMissionDetailsForSupervisorBinding
import com.example.souzmerch.shared.extensions.applyVisibility
import com.example.souzmerch.shared.extensions.convertLongToTime
import com.example.souzmerch.ui.BaseFragment
import com.example.souzmerch.ui.viewModels.MissionDetailsViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class SupervisorMissionDetails : BaseFragment<FragmentMissionDetailsForSupervisorBinding>(
    FragmentMissionDetailsForSupervisorBinding::inflate
) {

    val args: SupervisorMissionDetailsArgs by navArgs()

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

            val storageRef = mission.photo?.let { photo ->
                Firebase.storage.reference.child("file").child(
                    photo
                )
            }

            val localFile = File.createTempFile("tempImage", "jpg")
            storageRef?.getFile(localFile)?.addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                binding.ivTaskImage.setImageBitmap(bitmap)
            }

            if (mission.startTime?.isNotEmpty() == true)
                binding.tvStartTime.text = mission.startTime.toLong().convertLongToTime()

        }
    }
}