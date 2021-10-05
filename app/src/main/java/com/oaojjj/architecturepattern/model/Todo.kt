package com.oaojjj.architecturepattern.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(var content: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    override fun toString(): String = content
}
