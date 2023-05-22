package com.example.souzmerch.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.souzmerch.R
import com.example.souzmerch.data.enums.MissionState
import com.example.souzmerch.data.model.Mission
import com.example.souzmerch.databinding.FragmentCreateMissionBinding
import com.example.souzmerch.shared.utils.ToastError
import com.example.souzmerch.ui.BaseFragment
import com.example.souzmerch.ui.fragments.createMissionFragment.CreateMissionFragmentArgs
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CreateMissionFragment :
    BaseFragment<FragmentCreateMissionBinding>(FragmentCreateMissionBinding::inflate) {

    private val args: CreateMissionFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {

            when {
                binding.etTask.text.toString().isEmpty() -> ToastError.toast(
                    requireActivity(),
                    binding.tilTask
                )
                binding.etAmount.text.toString().isEmpty() -> ToastError.toast(
                    requireActivity(),
                    binding.tilAmount
                )

                else -> {
                    createTask()
                }
            }
        }
    }

    private fun createTask() {

        val db = FirebaseFirestore.getInstance()

        val mission = Mission(
            id = UUID.randomUUID().toString(),
            taskSettingTime = System.currentTimeMillis().toString(),
            startTime = null,
            photo = null,
            product = binding.etTask.text.toString(),
            amount = binding.etAmount.text.toString().toInt(),
            status = MissionState.CREATE.name,
            comment = binding.etComment.text.toString(),
            shopId = args.shopId
        )

        val docRef = db.collection("mission").document(mission.id)
        docRef.set(mission)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Успешно загружено!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_crateMissionFragment_to_shopsFragment)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Ошибка! $e", Toast.LENGTH_SHORT).show()
            }
    }
}