package br.com.angelorobson.alternativescene.domain


data class Client(
    val uuid: String,
    val authUuid: String,
    val name: String,
    val documentNumber: Int,
    val email: String,
    val phoneNumber: String,
    val description: String
) {
    constructor() : this("", "", "", 0, "", "", "")
}