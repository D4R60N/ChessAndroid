package uhk.palecek.chess.data

import com.google.gson.annotations.SerializedName

data class SignInData(
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String,
)
