package br.com.angelorobson.alternativescene.domain.response

data class AuthResponse(
    val token: String,
    val userAppDto: UserResponse
)
