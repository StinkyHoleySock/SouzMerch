package com.example.souzmerch.data.model

data class User(
    val id: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val email: String,
    val merchandiser: Boolean,
    val executor: Boolean,
    val customer: Boolean,
) {
    constructor() : this("", "", "", "", "", false, false, false)
}
