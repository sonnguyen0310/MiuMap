package com.sng.miumap.model

data class Profile(val id: String, val firstName: String, val lastName: String, val email: String) {

    fun fullName() = "$firstName $lastName"

}