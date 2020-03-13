package com.sng.miumap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    val id: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val phoneNumber: String?,
    @SerializedName("avatar")
    val imageUrl: String
) : Parcelable {

    fun fullName() = "$firstName $lastName"

}