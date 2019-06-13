package br.com.angelorobson.alternativescene.domain.response

import br.com.angelorobson.alternativescene.domain.User

data class AuthResponse(
    val token: String,
    val user: User
)
