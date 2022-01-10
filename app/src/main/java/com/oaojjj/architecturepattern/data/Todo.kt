package com.oaojjj.architecturepattern.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Model class for a task.
 *
 * @param content content of the task
 * @param id      id of the task
 */
@Entity(tableName = "todo")
data class Todo(
    @ColumnInfo(name = "content") var content: String = "",
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long? = null
) {
    @ColumnInfo(name = "completed")
    var isCompleted: Boolean = false

    val isEmpty
        get() = content.isEmpty()
}
