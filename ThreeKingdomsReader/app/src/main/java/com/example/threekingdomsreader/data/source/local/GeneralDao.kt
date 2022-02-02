package com.example.threekingdomsreader.data.source.local

import androidx.room.*
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
     * Insert generals from the "Generals.db".
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

    /**
     * Insert a general by id.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeneral(general: General)

    /**
     * Delete a general by id.
     */
    @Query("DELETE FROM generals WHERE id=:generalId")
    fun deleteGeneralById(generalId: Long)

}