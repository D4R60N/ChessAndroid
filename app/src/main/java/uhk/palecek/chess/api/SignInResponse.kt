package cz.uhk.fim.cryptoapp.api

import com.google.gson.annotations.SerializedName

data class SignInResponse<T>(
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String
)
