package com.example.souzmerch.ui.fragments.loginFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.souzmerch.R
import com.example.souzmerch.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Инициализация ViewBinding и авторизации
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            when {
                //Валидация полей
                binding.etEmail.text.toString().isEmpty() -> {
                    Toast.makeText(activity, "${binding.etEmail.hint} is empty!", Toast.LENGTH_SHORT)
                        .show()
                }

                binding.etPassword.text.toString().isEmpty() -> {
                    Toast.makeText(activity, "${binding.etPassword.hint} is empty!", Toast.LENGTH_SHORT)
                        .show()
                }

                //Вход в систему
                else -> firebaseSignIn(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
        }
    }

    //Метод для входа в Firebase
    private fun firebaseSignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                findNavController().navigate(R.id.action_loginFragment_to_fragmentIntermediate)
            } else {
                Toast.makeText(
                    activity, "Authentication failed. ${task.exception}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    //Методы для тестирования
    fun validateUser(email: String, password: String): Boolean {
        return (email.isNotEmpty() && password.isNotEmpty() && password.length >= 8)
    }

    fun isEmailValid(email: String): Boolean {
        return email.contains("mail", false)
    }
}