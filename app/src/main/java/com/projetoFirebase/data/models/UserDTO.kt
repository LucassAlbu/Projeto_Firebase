package com.projetoFirebase.data.models

data class UserDTO(
    val name: String? = "",
    val cpf: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val password: String? = "",
    val birthDate: String? = ""
)
