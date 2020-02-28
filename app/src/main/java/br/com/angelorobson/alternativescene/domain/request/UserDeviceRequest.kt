package br.com.angelorobson.alternativescene.domain.request

data class UserDeviceRequest(
    val userId: Long,
    val deviceId: String
)