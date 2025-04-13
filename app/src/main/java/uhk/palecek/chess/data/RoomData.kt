package uhk.palecek.chess.data

import com.google.gson.annotations.SerializedName

data class RoomData(
    @SerializedName("roomId") val roomId: String,
    @SerializedName("open") val open: Boolean,
    @SerializedName("players") val players: List<PlayerData>,
    @SerializedName("gameId") val gameId: String,
)
