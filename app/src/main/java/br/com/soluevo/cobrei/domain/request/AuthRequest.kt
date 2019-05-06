package br.com.soluevo.cobrei.domain.request

data class AuthRequest(
    var username: String = "",
    var password: String = ""
)