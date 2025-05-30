package uhk.palecek.chess.consts

//objekt pro konstanty všech routes
//objekt umožňuje statický přístup (v podstatě singleton)
object Routes {
    const val Game = "game/{side}/{id}"
    const val JoinGame = "join-game"
    const val MatchHistory = "match-history"
    const val Home = "home"
    const val SignIn = "sign-in"
    const val SignUp = "sign-up"
    const val Settings = "settings"

    fun game(side: String, id: String): String {
        return "game/$side/$id"
    }
}