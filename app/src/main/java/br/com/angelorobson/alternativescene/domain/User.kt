package br.com.angelorobson.alternativescene.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val uuid: String,
    val name: String,
    val username: String,
    val documentNumber: Int,
    val email: String,
    val phoneNumber: String,
    val bankSlug: String,
    val bankAgency: Int,
    val bankAccount: Int,
    val bankHolder: String,
    val bankDocumentNumber: Int,
    val data: User
) : Parcelable
