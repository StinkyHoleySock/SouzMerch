package com.example.souzmerch.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.souzmerch.R
import com.example.souzmerch.databinding.FragmentIntermediateBinding
import com.example.souzmerch.ui.BaseFragment
import com.example.souzmerch.ui.viewModels.RoleViewModel
import com.google.firebase.auth.FirebaseAuth

class IntermediateFragment :
    BaseFragment<FragmentIntermediateBinding>(FragmentIntermediateBinding::inflate) {

    private lateinit var roleViewModel: RoleViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roleViewModel = ViewModelProvider(this)[RoleViewModel::class.java]

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        roleViewModel.getUserRoleById(userId ?: "")

        //Принятие решения о навигации
        roleViewModel.userRole.observe(viewLifecycleOwner) {
            when (it) {
                "merchandiser" -> {
                    findNavController().navigate(R.id.action_fragmentIntermediate_to_merchNavigationFragment)
                }
                "executor" -> {
                    findNavController().navigate(R.id.action_fragmentIntermediate_to_executorNavigationFragment)
                }
                "customer" -> {
                    findNavController().navigate(R.id.action_fragmentIntermediate_to_customerNavigationFragment)
                }
                else -> {
                    Toast.makeText(requireContext(), "Ошибка!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}