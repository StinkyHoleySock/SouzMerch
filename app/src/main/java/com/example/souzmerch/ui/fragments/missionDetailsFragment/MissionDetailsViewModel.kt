package com.example.souzmerch.ui.fragments.missionDetailsFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.souzmerch.data.model.Mission
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MissionDetailsViewModel : ViewModel() {

    private val _mission = MutableLiveData<Mission>()
    val mission: LiveData<Mission> get() = _mission
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val db = Firebase.firestore

    fun getMission(missionId: String) {
        _isLoading.value = true
        db.collection("mission").document(missionId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val mission = documentSnapshot.toObject(Mission::class.java)

                _mission.postValue(mission!!)
                _isLoading.value = false
                Log.d("develop", "mission: $mission")
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                Log.d("develop", "Error getting mission", exception)
            }
    }

    fun updateMissionStatus(missionId: String, newStatus: String) {
        val missionRef = db.collection("mission").document(missionId)
        missionRef
            .update("status", newStatus)
            .addOnSuccessListener {
                Log.d("updateStatus", "Mission $missionId status updated to $newStatus")
            }
            .addOnFailureListener { e ->
                Log.e("updateStatus", "Error updating mission $missionId status", e)
            }
    }

    fun updateMissionStartTime(missionId: String, startTime: String) {
        val missionRef = db.collection("mission").document(missionId)
        missionRef
            .update("startTime", startTime)
            .addOnSuccessListener {
                Log.d("updateStatus", "Mission $missionId status updated to $startTime")
            }
            .addOnFailureListener { e ->
                Log.e("updateStatus", "Error updating mission $missionId status", e)
            }
    }
}