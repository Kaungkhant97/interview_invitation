package com.example.invitation.data

import com.example.invitation.data.entity.ErrorResponse
import com.example.invitation.data.entity.User
import com.google.gson.Gson
import java.lang.Exception
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val invitationApi: InvitationApi,
    private val userLocalDataStore: UserLocalDataStore
) : UserRepository {

    private val gson by lazy { Gson() }

    override suspend fun signup(name: String, email: String): Result<Unit> {
        val result = invitationApi.signup(User(name, email))
        return if (result.isSuccessful) {
            userLocalDataStore.setRegister(true)
            Result.success(Unit)

        } else {
            val errorResponseJson = result.errorBody()?.string()
            if (!errorResponseJson.isNullOrEmpty()) {
                val errorResponse = gson.fromJson(errorResponseJson, ErrorResponse::class.java)
                val errorMessage = errorResponse.errorMessage
                Result.failure(Exception(errorMessage))

            } else {
                Result.failure(Exception("Something went wrong"))
            }
        }
    }

    override suspend fun isUserRegistered(): Boolean = userLocalDataStore.isRegistered()

    override suspend fun removeUserRegistered() {
            userLocalDataStore.setRegister(false)
    }

}