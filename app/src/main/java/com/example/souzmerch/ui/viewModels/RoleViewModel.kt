package com.example.souzmerch.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.souzmerch.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RoleViewModel : ViewModel() {

    private val _userRole = MutableLiveData<String>()
    val userRole: LiveData<String> get() = _userRole

    private val db = Firebase.firestore

    fun getUserRoleById(userId: String) {
        db.collection("user")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)
                    when {
                        user?.merchandiser == true -> _userRole.value = "merchandiser"
                        user?.executor == true -> _userRole.value = "executor"
                        user?.customer == true -> _userRole.value = "customer"
                        else -> _userRole.value = "unknown"
                    }
                } else {
                    _userRole.value = "unknown"
                }
            }
            .addOnFailureListener { exception ->
                _userRole.value = "unknown"
                Log.d("UserRoleViewModel", "Error getting user role", exception)
            }
    }
}