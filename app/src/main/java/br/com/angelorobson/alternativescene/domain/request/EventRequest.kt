package br.com.angelorobson.alternativescene.domain.request

import java.util.*
import kotlin.collections.ArrayList

data class EventRequest(
    var imageUrl: String = "",
    var userAppId: Long = 0,
    var eventDates: MutableList<DateEvent> = mutableListOf(),
    var locality: String = "",
    var cityName: String = "",
    var address: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0
)

data class DateEvent(
    var date: Date = Date(),
    var hour: String = "",
    var price: Double = 0.0
)
