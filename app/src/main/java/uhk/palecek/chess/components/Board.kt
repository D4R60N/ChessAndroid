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
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Rank
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move

@Composable
fun BoardComponent() {

    var selectedSpace: Square? by remember { mutableStateOf(null) }
    val highlightedSpaces: MutableSet<Square> by remember { mutableStateOf(mutableSetOf<Square>()) }
    val map = HashMap<Square, Unit>()
    val board by remember { mutableStateOf(Board()) }
    var index = 0
    var side = board.sideToMove
    if (!board.isMated) {
        Box(
            Modifier
                .fillMaxSize()
                .aspectRatio(1.1f)
        ) {
            Column {
                for (rank in 8 downTo 1) {
                    Row(modifier = Modifier.weight(1f)) {
                        for (file in 1..8) {
                            val square = Square.squareAt(63 - index)
                            index++
                            val isHeighlighted = highlightedSpaces.contains(square)
                            map.put(
                                square,
                                GameSquare(
                                    (rank + file) % 2 == 0,
                                    board.getPiece(square),
                                    isHeighlighted,
                                    square == selectedSpace
                                ) { ->
                                    val piece = board.getPiece(square)
                                    if (piece != null && piece.pieceSide == side) {
                                        highlightedSpaces.clear()

                                        board.legalMoves().forEach { move: Move ->
                                            if (move.from.name == square?.name) {
                                                highlightedSpaces.add(move.to)
                                            }
                                        }
                                        selectedSpace = square
                                    } else if (isHeighlighted) {
                                        val rank =
                                            if (side == Side.WHITE) Rank.RANK_8 else Rank.RANK_1
                                        val move: Move
                                        if (square.rank == rank) {
                                            val promote =
                                                if (side == Side.WHITE) Piece.WHITE_QUEEN else Piece.BLACK_QUEEN
                                            move = Move(selectedSpace, square, promote)
                                        } else {
                                            move = Move(selectedSpace, square)
                                        }
                                        board.doMove(move)
                                        selectedSpace = null
                                        highlightedSpaces.clear()
                                    }
                                }
                            )
                        }
                    }

                }
            }
        }
    }
    else {
        Text("The ${side.flip()} has won!")
    }
}