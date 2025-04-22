package com.example.simpleaplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class ProfileDataclass (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val email: String,
    val password: String,
    val nohp: String,
    val alamat: String
)
