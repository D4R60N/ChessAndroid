package uhk.palecek.chess.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.bhlangonijr.chesslib.Piece
import uhk.palecek.chess.ui.theme.DarkSquareColor
import uhk.palecek.chess.ui.theme.LightSquareColor
import androidx.compose.ui.res.painterResource
import uhk.palecek.chess.R
import uhk.palecek.chess.ui.theme.HighlightedSquareColor
import uhk.palecek.chess.ui.theme.SelectedSquareColor

@Composable
fun GameSquare(isDarkSquare: Boolean, piece: Piece?, isHighlighted: Boolean, isSelected: Boolean, onSelect: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(color = if (isSelected) SelectedSquareColor else(if (isHighlighted) HighlightedSquareColor else (if (isDarkSquare) DarkSquareColor else LightSquareColor)))
            .clickable {
                onSelect()
            }
    ) {
        if (piece !== null && piece !== Piece.NONE)
            Image(
                painter = painterResource(selectPiece(piece)),
                contentDescription = "piece"
            )
    }
}

fun selectPiece(piece: Piece): Int {
    when (piece) {
        Piece.WHITE_PAWN -> return R.drawable.wp
        Piece.WHITE_KNIGHT -> return R.drawable.wkn
        Piece.WHITE_BISHOP -> return R.drawable.wb
        Piece.WHITE_ROOK -> return R.drawable.wr
        Piece.WHITE_QUEEN -> return R.drawable.wq
        Piece.WHITE_KING -> return R.drawable.wk
        Piece.BLACK_PAWN -> return R.drawable.bp
        Piece.BLACK_KNIGHT -> return R.drawable.bkn
        Piece.BLACK_BISHOP -> return R.drawable.bb
        Piece.BLACK_ROOK -> return R.drawable.br
        Piece.BLACK_QUEEN -> return R.drawable.bq
        Piece.BLACK_KING -> return R.drawable.bk
        Piece.NONE -> return -1
    }
}