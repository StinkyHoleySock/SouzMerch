package com.example.souzmerch.ui.fragments.createMissionFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.souzmerch.data.model.Mission
import com.example.souzmerch.databinding.FragmentCreateMissionBinding
import com.example.souzmerch.shared.utils.ToastError
import com.example.souzmerch.ui.fragments.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CreateMissionFragment :
    BaseFragment<FragmentCreateMissionBinding>(FragmentCreateMissionBinding::inflate) {

    private val args: CreateMissionFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {

            when {
                //Валидация полей
                binding.etTask.text.toString().isEmpty() -> ToastError.toast(
                    requireActivity(),
                    binding.tilTask
                )
                binding.etAmount.text.toString().isEmpty() -> ToastError.toast(
                    requireActivity(),
                    binding.tilAmount
                )

                //Регистрация
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
            status = "Создан",
            comment = null,
            shopId = args.shopId
        )

        val docRef = db.collection("mission").document(mission.id)
        docRef.set(mission)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Успешная регистрация!", Toast.LENGTH_SHORT)
                    .show()
                Log.d("develop", "User saved successfully")
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Ошибка регистрации!", Toast.LENGTH_SHORT)
                    .show()
                Log.e("develop", "Error saving user", e)
            }
    }
}