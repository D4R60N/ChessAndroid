package uhk.palecek.chess.data

import com.google.gson.annotations.SerializedName

data class GamesData(
    @SerializedName("games") val games: List<GameData>,
)
data class GameData(
    @SerializedName("roomId") val roomId: String,
    @SerializedName("open") val open: Boolean,
    @SerializedName("players") val players: List<PlayerData>,
)
