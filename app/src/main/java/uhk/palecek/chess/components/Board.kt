package uhk.palecek.chess.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move

@Composable
fun BoardComponent() {
    var selectedSpace: Square? by remember { mutableStateOf(null) }
    val highlightedSpaces: MutableSet<Square> by remember { mutableStateOf(mutableSetOf<Square>()) }
    var text by remember { mutableStateOf("rewt") }
    val map = HashMap<Square, Unit>()
    val board = Board()
    var index = 0;
    Text(text)
    Box(
        Modifier
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        Column {
            for (rank in 8 downTo 1) {
                Row(modifier = Modifier.weight(1f)) {
                    for (file in 1..8) {
                        val square = Square.squareAt(63-index)
                        index++;
                        map.put(
                            square,
                            GameSquare(
                                (rank + file) % 2 == 0,
                                board.getPiece(square),
                                highlightedSpaces.contains(square),
                                square == selectedSpace,
                                { ->
                                    highlightedSpaces.clear()

                                    board.legalMoves().forEach { move: Move ->
                                        if (move.from.name == square?.name) {
                                            text = move.to.name
                                            highlightedSpaces.add(move.to)
                                        }
                                    }
                                    selectedSpace = square
                                })
                        )
                    }
                }

            }
        }
    }
}