package br.com.angelorobson.alternativescene.domain.request

data class AuthRequest(
    var username: String = "",
    var password: String = ""
)