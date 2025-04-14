package uhk.palecek.chess.api

import com.google.gson.annotations.SerializedName

data class ChessResponse<T>(
    @SerializedName("docs") val data: T
)
