package uhk.palecek.chess.repository

import io.objectbox.Box
import uhk.palecek.chess.data.UserData


class UserRepository(private val userBox: Box<UserData>) {
    fun setUserData(user: UserData) {
        val existingEntity = userBox.query()
            .build().findFirst()

        if (existingEntity != null) {
            existingEntity.token = user.token
            existingEntity.username = user.username
            userBox.put(existingEntity)
        } else {
            val userData = UserData(id = user.id, username = user.username, token = user.token)
            userBox.put(userData)
        }

    }

    fun removeUserData() {
        val query = userBox.query()
            .build()
        val result = query.findFirst()
        if (result != null) {
            userBox.remove(result)
        }
        query.close()
    }

    fun getUserData(): UserData? {
        return userBox.query()
            .build().findFirst()
    }
}