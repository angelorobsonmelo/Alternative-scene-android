package br.com.soluevo.cobrei.domain.response

data class AuthResponse(
    val token: String,
    val user: User
)
