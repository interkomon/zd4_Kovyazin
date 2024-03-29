package com.bignerdranch.android.pract_19_1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Crime(@PrimaryKey var id: UUID = UUID.randomUUID())
{
    var title: String = ""
    var date: Date = Date()
    var isSolved: Boolean = false
    var requiresPolice: Boolean = false
    var suspect: String=""

}
