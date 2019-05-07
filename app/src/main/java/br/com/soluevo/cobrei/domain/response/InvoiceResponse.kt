package br.com.soluevo.cobrei.domain.response

import br.com.soluevo.cobrei.domain.Client

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