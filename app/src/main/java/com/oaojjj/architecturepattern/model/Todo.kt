package com.oaojjj.architecturepattern.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val content: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    override fun toString(): String {
        return content
    }
}
