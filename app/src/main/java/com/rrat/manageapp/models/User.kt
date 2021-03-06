package com.rrat.manageapp.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val fcmToken: String = "",
    var selected: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!
    ) {
    }

    override fun describeContents() = 0

    companion object : Parceler<User> {

        override fun User.write(parcel: Parcel, flags: Int) = with(parcel){
            writeString(id)
            writeString(name)
            writeString(email)
            writeString(image)
            writeLong(mobile)
            writeString(fcmToken)
        }

        override fun create(parcel: Parcel): User {
            return User(parcel)
        }
    }
}