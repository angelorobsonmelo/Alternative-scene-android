package br.com.soluevo.cobrei.domain.response

import br.com.soluevo.cobrei.domain.User

data class AuthResponse(
    val token: String,
    val user: User
)
