package com.example.invitation.data

interface UserLocalDataStore {

    suspend fun setRegister(boolean: Boolean)

    suspend fun isRegistered(): Boolean
}