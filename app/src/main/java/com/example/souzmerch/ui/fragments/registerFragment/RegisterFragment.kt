package com.example.souzmerch.ui.fragments.registerFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.souzmerch.R
import com.example.souzmerch.databinding.FragmentRegisterBinding
import com.example.souzmerch.shared.utils.ToastError.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Инициализация binding
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {


            btnRegister.setOnClickListener {
                when {
                    //Валидация полей
                    etName.text.toString().isEmpty() -> toast(requireActivity(), tilName)
                    etSurname.text.toString().isEmpty() -> toast(requireActivity(), tilSurname)
                    etPatronymic.text.toString().isEmpty() -> toast(
                        requireActivity(),
                        tilPatronymic
                    )
                    etEmail.text.toString().isEmpty() -> toast(requireActivity(), tilEmail)
                    etPassword.text.toString().isEmpty() -> toast(requireActivity(), tilPassword)

                    //Регистрация
                    else -> firebaseSignUp(
                        name = etName.text.toString(),
                        surname = etSurname.text.toString(),
                        patronymic = etPatronymic.text.toString(),
                        email = etEmail.text.toString(),
                    )
                }

            }
        }
    }

    //Метод для регистрации пользователя
    private fun firebaseSignUp(
        name: String,
        surname: String,
        patronymic: String,
        email: String,
    ) {

        val db = FirebaseFirestore.getInstance()

        auth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val user = FirebaseAuth.getInstance().currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(
                        binding.etName.text.toString() + ' ' + binding.etSurname.text.toString()
                    ).build()
                user!!.updateProfile(profileUpdates)

                //Создание объекта пользователя
//                val userToDB = User(
//                    id = user.uid,
//                    name = name,
//                    surname = surname,
//                    patronymic = patronymic,
//                    email = email,
//                    leader = false,
//                    superUser = superUser,
//                    fcmToken = ""
//                )
                //Запись пользователя в базу данных
//                val docRef = db.collection("user").document(userToDB.id)
//                docRef.set(userToDB)
//                    .addOnSuccessListener { Log.d("develop", "User saved successfully") }
//                    .addOnFailureListener { e -> Log.e("develop", "Error saving user", e) }

//                findNavController().navigate(R.id.action_registerFragment_to_navigationFragment)

            } else {
                Toast.makeText(
                    activity, "Authentication failed. ${task.exception}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //Метод для проверки валидности полей
    fun validateUser(
        username: String,
        surname: String,
        email: String,
        password: String
    ): Boolean {

        return (username.isNotEmpty() && surname.isNotEmpty()
                && email.isNotEmpty() && password.isNotEmpty())
    }

    //Методы для тестирования
    fun checkRules(isChecked: Boolean): String {
        return if (isChecked) "Rules is accepted"
        else "Rules not accepted"
    }

    fun validatePassword(password: String): Boolean {
        return (password.length >= 8)
    }

}