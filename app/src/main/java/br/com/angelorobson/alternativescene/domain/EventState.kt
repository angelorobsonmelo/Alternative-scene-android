package br.com.angelorobson.alternativescene.domain

data class EventState constructor(
    val eventId: Long,
    val favorite: Boolean,
    val clickOnBackPressed: Boolean
) {
    constructor(clickOnBackPressed: Boolean) : this(0, false, false)
}