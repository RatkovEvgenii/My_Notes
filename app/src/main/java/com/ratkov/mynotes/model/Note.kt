package com.ratkov.mynotes.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(val id: String = "",
                val title: String = "",
                val note: String = "",
                val rnds: Int = (1..7).random(),
                //val color: Color = Color.BLUE,
            val color: Color = when(rnds) {
                1 ->   Color.WHITE
                2 -> Color.VIOLET
                3->  Color.YELLOW
                4-> Color.RED
                5->  Color.PINK
                6->  Color.GREEN
                7->  Color.BLUE
                else -> Color.WHITE
            },
                val lastChanged: Date = Date()): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

enum class Color {
    WHITE,
    YELLOW,
    GREEN,
    BLUE,
    RED,
    VIOLET,
    PINK
}