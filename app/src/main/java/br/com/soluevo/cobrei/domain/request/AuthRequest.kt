package br.com.soluevo.cobrei.domain.request

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    var email: String = "",
    @SerializedName("grant_type")
    var grantType: String = "password",
    var password: String = ""
)