package br.com.angelorobson.alternativescene.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Event(
    val id: Long,
    val title: String,
    val description: String,
    val photoUrl: String,
    val locality: Locality,
    val eventDates: List<EventDate>,
    val musicalGenres: List<MusicalGenre>,
    val category: Category,
    val status: Boolean,
    val userApp: UserApp,
    val eventDate: String,
    val eventLocation: String
) : Parcelable

@Parcelize
data class Locality(
    val id: Long,
    val name: String,
    val address: String,
    val city: City
) : Parcelable

@Parcelize
data class City(
    val id: Long,
    val name: String,
    val state: State
) : Parcelable

@Parcelize
data class State(
    val id: Long,
    val name: String,
    val uf: String
) : Parcelable

@Parcelize
data class EventDate(
    val id: Long,
    val date: Date,
    val hour: String,
    val price: Double,
    val eventDateAndHourToString: String
) : Parcelable

@Parcelize
data class MusicalGenre(
    val id: Long,
    val name: String
) : Parcelable

@Parcelize
class Category(
    val id: Long,
    val name: String
) : Parcelable


@Parcelize
data class UserApp(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val email: String
) : Parcelable
