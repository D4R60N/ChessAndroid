package uhk.palecek.chess.consts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector, val screenRoute: String) {
    object JoinGame : BottomNavItem("JoinGame", Icons.Filled.PlayArrow, Routes.JoinGame)
    object Home : BottomNavItem("Home", Icons.Filled.Home, Routes.Home)
    object MatchHistory : BottomNavItem("MatchHistory", Icons.Filled.DateRange, Routes.MatchHistory)
    object SignIn : BottomNavItem("Sign In", Icons.Filled.Person, Routes.SignIn)
    object SignUp : BottomNavItem("Sign Up", Icons.Outlined.Person, Routes.SignUp)
}