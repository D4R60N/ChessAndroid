package uhk.palecek.chess.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Header(text: String) {
    Text(text = text, fontWeight = FontWeight.Bold, fontSize = 30.sp)
}