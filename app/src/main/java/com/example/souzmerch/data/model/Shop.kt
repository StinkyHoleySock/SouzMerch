package com.example.souzmerch.data.model

data class Shop(
    val id: String,
    val address: String,
    val merchandiser: String,
    val lat: Double,
    val lon: Double
) {
    constructor() : this("", "", "", 0.0, 0.0)
}