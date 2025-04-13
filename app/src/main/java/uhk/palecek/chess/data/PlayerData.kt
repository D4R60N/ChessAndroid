package uhk.palecek.chess.data

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class PlayerData(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("iat") val iat: Long,
    @SerializedName("exp") val exp: Long,
    @SerializedName("socketId") val socketId: String,
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("username", username)
        json.put("iat", iat)
        json.put("exp", exp)
        return json
    }
}