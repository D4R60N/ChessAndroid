package uhk.palecek.chess.data

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class MatchHistory(
    @SerializedName("players") val players: List<String>,
    @SerializedName("winner") val winner: String?,
    @SerializedName("date") val date: Instant,
)