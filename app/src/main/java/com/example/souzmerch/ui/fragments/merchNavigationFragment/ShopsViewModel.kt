package com.example.souzmerch.ui.fragments.merchNavigationFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.souzmerch.data.model.Shop
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShopsViewModel : ViewModel() {

    private val _shopList = MutableLiveData<List<Shop>>()
    val shopList: LiveData<List<Shop>> get() = _shopList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val db = Firebase.firestore

    fun getShopsForMerchandiser(merchandiserId: String) {
        _isLoading.value = true
        db.collection("shop")
            .whereEqualTo("merchandiser", merchandiserId)
            .get()
            .addOnSuccessListener { result ->
                val shops = result.toObjects(Shop::class.java)

                _shopList.postValue(shops)
                _isLoading.value = false
                Log.d("develop", "shops: $shops")
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                Log.d("develop", "Error getting shops", exception)
            }
    }
}