package com.example.souzmerch.shared.utils

import android.content.Context
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

//Метод для указания ошибки заполнения поля
object ToastError {
    fun toast(context: Context, field: TextInputLayout) {
        Toast.makeText(
            context,
            "Поле '${field.hint.toString()}' не может быть пустым!",
            Toast.LENGTH_SHORT
        ).show()
    }
}