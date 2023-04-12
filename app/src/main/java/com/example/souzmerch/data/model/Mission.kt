package com.example.souzmerch.data.model

data class Mission(
    val id: String,
    val taskSettingTime: String,
    val startTime: String?,
    val photo: String?,
    val product: String,
    val amount: Int,
    val status: String,
    val comment: String?
) {
    constructor() : this("", "", "", "", "", 0, "", "")
}
