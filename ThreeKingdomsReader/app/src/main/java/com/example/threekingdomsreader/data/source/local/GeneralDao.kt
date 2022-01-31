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
    @Query("SELECT * FROM generals")
    fun getGenerals(): List<General>

    /**
     * Insert generals from the "generals.db".
     *
     * @param generals the generals to add
     */
    @Insert
    fun addGenerals(generals: List<General>)


    /**
     * Select a general by id.
     *
     * @param generalId the general id.
     * @return the general with generalId.
     */
    @Query("SELECT * FROM generals WHERE id = :generalId")
    fun getGeneralById(generalId: Long?): General?

}