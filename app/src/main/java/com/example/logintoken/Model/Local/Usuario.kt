package com.example.logintoken.Model.Local

import com.example.logintoken.Model.Local.Transaccion

data class Usuario( val nombre: String,
val apellido: String,
val email: String,
val password: String,
val imgPerfil: Int = 0,
//val imgPerfil: Uri? = null,
val saldo: Double = 1000.0,
val transacciones: MutableList<Transaccion> = mutableListOf()
)