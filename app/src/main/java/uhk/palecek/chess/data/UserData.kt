package uhk.palecek.chess.data

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("token") val token: String,
    @SerializedName("username") val username: String,
)
