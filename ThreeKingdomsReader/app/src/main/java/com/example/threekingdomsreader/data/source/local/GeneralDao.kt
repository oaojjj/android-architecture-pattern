package com.example.threekingdomsreader.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.threekingdomsreader.data.General

/**
 * Data Access Object for the generals table.
 */
@Dao
interface GeneralDao {
    /**
     * Select all generals from the generals table.
     *
     * @return all generals.
     */
    @Query("SELECT * FROM generals") fun getGenerals(): List<General>

    /**
     * insert generals from the generals.json(init).
     */
    @Insert
    fun addGenerals(generals:List<General>)

}