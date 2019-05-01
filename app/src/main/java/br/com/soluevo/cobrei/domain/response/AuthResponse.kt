package br.com.soluevo.cobrei.domain.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("created_at")
    val createdAt: Date
)