package uhk.palecek.chess.data

import com.google.gson.annotations.SerializedName

data class SignData(
    @SerializedName("user") val username: User,
)
data class User(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
)
