package com.platform6ix.p6cms.presentation.data.model



data class RegisterResponse(
    val email: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val username: String
)