package com.example.invitation.data.entity

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("errorMessage")
    val errorMessage: String
)