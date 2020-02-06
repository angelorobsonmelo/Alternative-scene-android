package br.com.angelorobson.alternativescene.domain.request

data class AuthRequest(
    var email: String = "",
    var password: String = ""
)