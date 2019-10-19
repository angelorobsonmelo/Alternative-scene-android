package br.com.angelorobson.alternativescene.domain.response

import java.util.*

data class UserResponse(
    var id: Long,
    var email: String,
    var imageUrl: String,
    var name: String,
    var googleAccountId: String,
    var registrationDate: Date
)