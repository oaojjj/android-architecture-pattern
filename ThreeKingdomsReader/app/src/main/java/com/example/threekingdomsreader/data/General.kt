package com.example.threekingdomsreader.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "generals")
data class General @JvmOverloads constructor(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "sex") var sex: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "belong") var belong: String = "",
    @ColumnInfo(name = "position") var position: String = "",
    @ColumnInfo(name = "birth") var birth: String = "0",
    @ColumnInfo(name = "death") var death: String = "0",
    @ColumnInfo(name = "description") var description: String = "",
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long?
) : Serializable