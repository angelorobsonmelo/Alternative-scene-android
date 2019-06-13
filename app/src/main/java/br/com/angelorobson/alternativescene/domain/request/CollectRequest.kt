package br.com.angelorobson.alternativescene.domain.request

import java.math.BigDecimal

class CollectRequest(
    var uuid: String,
    var authUuid: String,
    var clientUuid: String,
    var value: BigDecimal,
    @Transient
    var valueCurrency: String,
    var slug: String,
    var description: String,
    var status: Boolean
) {
    constructor() : this("", "", "", BigDecimal.valueOf(0), "", "", "", false)
}