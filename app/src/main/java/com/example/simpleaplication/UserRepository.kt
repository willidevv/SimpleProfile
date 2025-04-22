package com.example.simpleaplication.data

import com.example.simpleaplication.ProfileDAO
import com.example.simpleaplication.ProfileDataclass

class UserRepository(private val profileDAO: ProfileDAO) {
    suspend fun insertUser(user: ProfileDataclass) = profileDAO.insert(user)

    suspend fun updateUser(user: ProfileDataclass) = profileDAO.updateUser(user)

    suspend fun getUserByEmail(email: String): ProfileDataclass? =
        profileDAO.getUserByEmail(email)
}