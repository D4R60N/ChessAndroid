package uhk.palecek.chess.data

import com.google.gson.annotations.SerializedName

data class UserStatsData(
    @SerializedName("username") val username: String,
    @SerializedName("elo") val elo: Number,
)
