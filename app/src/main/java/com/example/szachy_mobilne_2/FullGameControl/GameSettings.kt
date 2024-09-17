package com.example.szachy_mobilne_2.FullGameControl

import android.os.Parcel
import android.os.Parcelable

data class GameSettings(
    val opponentName: String,
    val color: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(opponentName)
        parcel.writeString(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameSettings> {
        override fun createFromParcel(parcel: Parcel): GameSettings {
            return GameSettings(parcel)
        }

        override fun newArray(size: Int): Array<GameSettings?> {
            return arrayOfNulls(size)
        }
    }
}