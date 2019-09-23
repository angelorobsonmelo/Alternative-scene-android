package br.com.angelorobson.alternativescene.domain.request

data class UserRequest(
    private val dateBird: String,
    private val email: String,
    private val gender: String,
    private val googleAccountId: String,
    private val imageUrl: String,
    private val name: String,
    private val password: String
) {
    constructor(email: String, password: String, googleAccountId: String, imageUrl: String, name: String) : this(
        "",
        email,
        "MALE",
        googleAccountId,
        imageUrl,
        name,
        password
    )
}