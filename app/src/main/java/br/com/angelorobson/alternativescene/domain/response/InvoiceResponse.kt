package br.com.angelorobson.alternativescene.domain.response

import br.com.angelorobson.alternativescene.domain.Client

data class InvoiceResponse(
    val uuid: String,
    val authUuid: String,
    val client: Client,
    val value: Int,
    val bankSlug: String,
    val description: String,
    val status: Boolean,
    val dateTime: String
)