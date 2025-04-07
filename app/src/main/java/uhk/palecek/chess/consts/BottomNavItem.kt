package uhk.palecek.chess.consts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector, val screenRoute: String) {
    object Game : BottomNavItem("Game", Icons.Filled.PlayArrow, Routes.Game)
    object SignIn : BottomNavItem("Sign In", Icons.Filled.Person, Routes.SignIn)
    object SignUp : BottomNavItem("Sign Up", Icons.Outlined.Person, Routes.SignUp)
    //object FavouriteCrypto : BottomNavItem("Favorite", Icons.Filled.FavoriteBorder, Routes.FavouriteCrypto)
}