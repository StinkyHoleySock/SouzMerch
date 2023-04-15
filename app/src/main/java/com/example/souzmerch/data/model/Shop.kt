package com.example.souzmerch.data.model

data class Shop(
    val id: String,
    val name: String,
    val merchandiser: String,
    val lat: String,
    val lon: String
) {
    constructor() : this("", "", "", "", "")
}
