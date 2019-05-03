package br.com.soluevo.cobrei.domain.request

data class AuthRequest(
    var email: String = "",
    var userName: String = "",
    var password: String = ""
)