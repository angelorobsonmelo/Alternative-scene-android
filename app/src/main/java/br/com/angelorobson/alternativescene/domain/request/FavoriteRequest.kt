package br.com.angelorobson.alternativescene.domain.request

data class FavoriteRequest(
    val userAppId: Long,
    val eventId: Long
)