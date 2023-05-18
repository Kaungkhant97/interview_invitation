package com.example.invitation.data

import com.example.invitation.data.entity.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface InvitationApi {

    @POST("/fakeAuth")
    suspend fun signup(@Body user: User): Response<Void>
}