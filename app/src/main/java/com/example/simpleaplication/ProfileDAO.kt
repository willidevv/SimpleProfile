package com.example.simpleaplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: ProfileDataclass)

    @Update
    suspend fun updateUser(user: ProfileDataclass)

    @Delete
    suspend fun delete(user: ProfileDataclass)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Int): ProfileDataclass

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): ProfileDataclass?
}