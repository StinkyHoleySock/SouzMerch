package com.example.souzmerch.ui.fragments.missionsFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.souzmerch.data.enums.MissionState
import com.example.souzmerch.data.model.Mission
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MissionsViewModel : ViewModel() {

    private val _missionList = MutableLiveData<List<Mission>>()
    val missionList: LiveData<List<Mission>> get() = _missionList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val db = Firebase.firestore

    fun getMissions(shopId: String) {
        _isLoading.value = true
        db.collection("mission")
            .whereEqualTo("shopId", shopId)
            .get()
            .addOnSuccessListener { result ->
                val missions = result.toObjects(Mission::class.java)

                _missionList.postValue(missions)
                _isLoading.value = false
                Log.d("develop", "missions: $missions")
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                Log.d("develop", "Error getting missions", exception)
            }
    }

    fun getUnverifiedMissions() {
        _isLoading.value = true
        db.collection("mission")
            .whereEqualTo("status", MissionState.VERIFICATION.name)
            .get()
            .addOnSuccessListener { result ->
                val missions = result.toObjects(Mission::class.java)

                _missionList.postValue(missions)
                _isLoading.value = false
                Log.d("develop", "missions: $missions")
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                Log.d("develop", "Error getting missions", exception)
            }
    }
}