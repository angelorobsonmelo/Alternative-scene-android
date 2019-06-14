package br.com.angelorobson.alternativescene.domain

import java.util.*

data class Event (
    val id: Long,
    val title: String,
    val description: String,
    val photoUrl: String,
    val locality: Locality,
    val eventDates: List<EventDate>,
    val musicalGenres: List<MusicalGenre>,
    val category: Category,
    val status: Boolean,
    val userApp: UserApp
    )

data class Locality (
    val id: Long,
    val name: String,
    val city: City)

data class City (
    val id: Long,
    val name: String,
    val state: State)

data class State (
    val id: Long,
    val name: String,
    val uf: String)


data class EventDate (
    val id: Long,
    val date: Date,
    val hour: String,
    val price: Double,
    val eventDateAndHourToString: String)

data class MusicalGenre (
    val id: Long,
    val name: String)

class Category (
    val id: Long,
    val name: String)

data class UserApp (
    val id: Long,
    val name: String,
    val imageUrl: String,
    val email: String
)
