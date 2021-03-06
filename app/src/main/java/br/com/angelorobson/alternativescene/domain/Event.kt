package br.com.angelorobson.alternativescene.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
data class Event(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val imageThumbUrl: String,
    val locality: Locality,
    val eventDates: List<EventDate>,
    val musicalGenres: List<MusicalGenre>,
    val category: Category,
    val status: String,
    val userApp: UserApp,
    val eventDate: String,
    val eventLocation: String,
    var favorite: Boolean
) : Parcelable

@Parcelize
data class Locality(
    val id: Long,
    val name: String,
    val address: String,
    val city: City
) : Parcelable, Serializable

@Parcelize
data class City(
    val id: Long,
    val name: String,
    val state: State
) : Parcelable, Serializable

@Parcelize
data class State(
    val id: Long,
    val name: String,
    val uf: String
) : Parcelable, Serializable

@Parcelize
data class EventDate(
    val id: Long,
    val date: Date,
    val hour: String,
    val price: Double,
    val eventDateAndHourToString: String
) : Parcelable, Serializable

@Parcelize
data class MusicalGenre(
    val id: Long,
    val name: String
) : Parcelable, Serializable

@Parcelize
class Category(
    val id: Long,
    val name: String
) : Parcelable, Serializable


@Parcelize
data class UserApp(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val email: String
) : Parcelable, Serializable
