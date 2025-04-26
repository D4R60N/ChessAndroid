package uhk.palecek.chess.data

import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class UserData(
    @Id var id: Long = 0,
    var token: String,
    var username: String,
)
