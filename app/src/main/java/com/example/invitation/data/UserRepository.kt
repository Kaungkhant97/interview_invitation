package com.example.invitation.data

interface UserRepository {

    suspend fun signup(name: String, email: String) : Result<Unit>

    suspend fun isUserRegistered(): Boolean

    suspend fun removeUserRegistered()
}