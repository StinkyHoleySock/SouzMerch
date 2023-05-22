package com.example.souzmerch.ui.viewModels

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

    private val _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop> get() = _shop

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

    fun getAllShops() {
        _isLoading.value = true
        db.collection("shop")
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

    fun getShopById(shopId: String) {

        db.collection("shop").document(shopId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val shop = document.toObject(Shop::class.java)
                    shop?.let { _shop.postValue(it) }
                } else {
                    Log.d("MissionViewModel", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("MissionViewModel", "getShopById failed with ", exception)
            }
    }
}