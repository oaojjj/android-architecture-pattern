package com.oaojjj.architecturepattern.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(var content: String = "") {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "checked")
    var checked: Boolean = false

    override fun toString(): String = content
}
