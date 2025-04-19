package uhk.palecek.chess.components

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.Animation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.PieceType
import com.github.bhlangonijr.chesslib.Rank
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import uhk.palecek.chess.consts.Routes
import uhk.palecek.chess.data.PlayerData
import uhk.palecek.chess.utils.SocketHandler

@Composable
fun BoardComponent(
    sideString: String,
    id: String,
    players: List<PlayerData>,
    navController: NavController
) {
    var selectedSpace: Square? by remember { mutableStateOf(null) }
    var side: Side = if (sideString == "white") Side.WHITE else Side.BLACK
    val highlightedSpaces: MutableSet<Square> by remember { mutableStateOf(mutableSetOf<Square>()) }
    val map = HashMap<Square, Unit>()
    val board by remember { mutableStateOf(Board()) }
    var index = 0
    val mSocket = SocketHandler.getSocket()
    var winner: String? by remember { mutableStateOf<String?>(null) }
    var gameOver: String by remember { mutableStateOf<String>("") }
    val gson = Gson()
    var recompose by remember { mutableStateOf(0) }

    fun makeAMove(move: Move) {
        board.doMove(move)
        if (board.isMated) {
            winner = if (board.sideToMove == Side.BLACK) players[0].id else players[1].id
            gameOver =
                "Checkmate! The ${if (board.sideToMove == Side.BLACK) "black" else "white"} wins!"
            val obj = JSONObject()
            obj.put("roomId", id)
            obj.put("winner", winner)
            mSocket.emit("endGame", obj)
        } else if (board.isDraw) {
            gameOver = "Draw!"
            val obj = JSONObject()
            obj.put("roomId", id)
            obj.put("winner", null)
            mSocket.emit("endGame", obj)
        }
    }

    LaunchedEffect(Unit) {
        mSocket.on("playerDisconnected") { args ->
            val player = gson.fromJson(args[0].toString(), PlayerData::class.java)
            Handler(Looper.getMainLooper()).post {
                winner = player.id
                gameOver = "${player.username} has disconnected"
            }
        }
        mSocket.on("endGame") { args ->
            val obj = JSONObject(args[0].toString())
            Handler(Looper.getMainLooper()).post {
                winner = obj.getString("winner")
                gameOver = "Checkmate! The ${side.flip()} wins!"
            }
        }
        mSocket.on("move") { args ->
            val obj = JSONObject(args[0].toString())
            val from = Square.fromValue(obj.getString("from").uppercase())
            val to = Square.fromValue(obj.getString("to").uppercase())
            val promotion = getPieceFromChar(
                if (obj.has("promotion")) obj.getString("promotion") else "NONE",
                side.flip()
            )
            val move = Move(from, to, promotion)
            makeAMove(move)
            recompose += 1;
        }

    }

    key(recompose) {
        if (!board.isMated) {
            Box(
                Modifier
                    .fillMaxSize()
            ) {
                Column {
                    for (rank in 8 downTo 1) {
                        Row(modifier = Modifier.weight(1f)) {
                            for (file in 1..8) {
                                val square = Square.squareAt(63 - index)
                                index++
                                val isHighlighted = highlightedSpaces.contains(square)
                                map.put(
                                    square,
                                    GameSquare(
                                        (rank + file) % 2 == 0,
                                        board.getPiece(square),
                                        isHighlighted,
                                        square == selectedSpace
                                    ) { ->
                                        val piece = board.getPiece(square)
                                        if (piece != null && piece.pieceSide == side && piece.pieceSide == board.sideToMove) {
                                            highlightedSpaces.clear()

                                            board.legalMoves().forEach { move: Move ->
                                                if (move.from.name == square?.name) {
                                                    highlightedSpaces.add(move.to)
                                                }
                                            }
                                            selectedSpace = square
                                        } else if (isHighlighted) {
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
                                            val moveJson = JSONObject()
                                            val moveData = JSONObject()
                                            moveData.put(
                                                "color",
                                                if (board.sideToMove == Side.WHITE) "w" else "b"
                                            )
                                            moveData.put("piece", getPiece(board.getPiece(square)))
                                            moveData.put("from", move.from.toString().lowercase())
                                            moveData.put("to", move.to.toString().lowercase())
                                            moveData.put("san", toSan(move, board).lowercase())
                                            moveData.put("flags", getMoveFlags(move, board))
                                            if (move.promotion != Piece.NONE) moveData.put(
                                                "promotion",
                                                "q"
                                            )
                                            moveData.put(
                                                "lan",
                                                "${move.from}${move.to}".lowercase()
                                            )
                                            moveData.put("before", board.fen)
                                            makeAMove(move)
                                            moveData.put("after", board.fen)
                                            moveJson.put("move", moveData)
                                            moveJson.put("room", id)
                                            mSocket.emit("move", moveJson)

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
        } else {
            Column()
            {
                Text("The ${side.flip()} has won! \uD83C\uDF89", fontSize = 24.sp)
                Button(onClick = {
                    navController.navigate(Routes.Home)
                }) {
                    Text("Back to Home")
                }
            }
        }
    }
}

fun getPiece(piece: Piece): String {
    when (piece.pieceType) {
        PieceType.PAWN -> return "p"
        PieceType.KING -> return "k"
        PieceType.QUEEN -> return "q"
        PieceType.BISHOP -> return "b"
        PieceType.KNIGHT -> return "n"
        PieceType.ROOK -> return "r"
        else -> "_"
    }
    return "_"
}

fun getPieceFromChar(char: String, side: Side): Piece {
    return when (char.lowercase()) {
        "p" -> if (side == Side.WHITE) Piece.WHITE_PAWN else Piece.BLACK_PAWN
        "k" -> if (side == Side.WHITE) Piece.WHITE_KING else Piece.BLACK_KING
        "q" -> if (side == Side.WHITE) Piece.WHITE_QUEEN else Piece.BLACK_QUEEN
        "b" -> if (side == Side.WHITE) Piece.WHITE_BISHOP else Piece.BLACK_BISHOP
        "n" -> if (side == Side.WHITE) Piece.WHITE_KNIGHT else Piece.BLACK_KNIGHT
        "r" -> if (side == Side.WHITE) Piece.WHITE_ROOK else Piece.BLACK_ROOK
        else -> Piece.NONE
    }
}

fun getMoveFlags(move: Move, board: Board): String {
    val flags = StringBuilder()

    val fromPiece = board.getPiece(move.from)
    val toPiece = board.getPiece(move.to)

    if (toPiece.pieceType != PieceType.NONE) {
        flags.append("c")
    }

    if (fromPiece.pieceType == PieceType.PAWN &&
        move.from.file != move.to.file && toPiece.pieceType == PieceType.NONE
    ) {
        flags.append("e")
        flags.append("c")
    }

    if (move.promotion != null) {
        flags.append("p")
    }

    if (fromPiece.pieceType == PieceType.PAWN) {
        val fromRank = move.from.rank.ordinal
        val toRank = move.to.rank.ordinal
        if (kotlin.math.abs(fromRank - toRank) == 2) {
            flags.append("b")
        }
    }

    if (flags.isEmpty()) {
        flags.append("n")
    }

    return flags.toString()
}

fun toSan(move: Move, board: Board): String {
    val piece = board.getPiece(move.from)
    val pieceType = piece.pieceType
    val side = piece.pieceSide

    // Handle castling
    if (pieceType == PieceType.KING && move.from == Square.E1 && move.to == Square.G1 && side == Side.WHITE) return "O-O"
    if (pieceType == PieceType.KING && move.from == Square.E1 && move.to == Square.C1 && side == Side.WHITE) return "O-O-O"
    if (pieceType == PieceType.KING && move.from == Square.E8 && move.to == Square.G8 && side == Side.BLACK) return "O-O"
    if (pieceType == PieceType.KING && move.from == Square.E8 && move.to == Square.C8 && side == Side.BLACK) return "O-O-O"

    val targetPiece = board.getPiece(move.to)
    val isCapture = targetPiece.pieceType != PieceType.NONE ||
            (pieceType == PieceType.PAWN && move.from.file != move.to.file)

    val sb = StringBuilder()

    // Piece letter (empty for pawn)
    if (pieceType != PieceType.PAWN) {
        sb.append(getPieceChar(pieceType))
    } else if (isCapture) {
        // For pawn captures, show file of origin (e.g., exd5)
        sb.append(move.from.file.name.lowercase())
    }

    // Capture indicator
    if (isCapture) {
        sb.append("x")
    }

    // Target square
    sb.append(move.to.name.lowercase())

    // Promotion
    if (move.promotion != null) {
        sb.append("=")
        sb.append(getPieceChar(move.promotion.pieceType))
    }

    // You could add "+" or "#" here by checking for checks or checkmate if you want.

    return sb.toString()
}

fun getPieceChar(type: PieceType?): String {
    return when (type) {
        PieceType.KING -> "K"
        PieceType.QUEEN -> "Q"
        PieceType.ROOK -> "R"
        PieceType.BISHOP -> "B"
        PieceType.KNIGHT -> "N"
        PieceType.PAWN -> "" // usually not shown in SAN
        else -> ""
    }
}