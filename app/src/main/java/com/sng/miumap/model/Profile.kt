package com.sng.miumap.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String?,
    val imageUrl: String
) : Parcelable {

    fun fullName() = "$firstName $lastName"

}