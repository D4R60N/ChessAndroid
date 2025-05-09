package uhk.palecek.chess

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.uhk.fim.cryptoapp.networkModule
import cz.uhk.fim.cryptoapp.objectBoxModule
import cz.uhk.fim.cryptoapp.repositoryModule
import cz.uhk.fim.cryptoapp.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin
import uhk.palecek.chess.ui.theme.ChessTheme
import uhk.palecek.chess.consts.BottomNavItem
import uhk.palecek.chess.consts.Routes
import uhk.palecek.chess.screens.GameScreen
import uhk.palecek.chess.screens.HomeScreen
import uhk.palecek.chess.screens.JoinGameScreen
import uhk.palecek.chess.screens.MatchHistoryScreen
import uhk.palecek.chess.screens.SignInScreen
import uhk.palecek.chess.screens.SignUpScreen
import uhk.palecek.chess.ui.theme.DarkSquareColor
import uhk.palecek.chess.ui.theme.LightSquareColor
import uhk.palecek.chess.utils.SocketHandler
import uhk.palecek.chess.viewmodels.AuthState
import uhk.palecek.chess.viewmodels.UserViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(
                repositoryModule,
                viewModelModule,
                networkModule,
                objectBoxModule
            )
        }
        enableEdgeToEdge()
        setContent {
            ChessTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: UserViewModel = koinViewModel()) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(0) }
    var items by remember { mutableStateOf(listOf<BottomNavItem>()) }
    val username by viewModel.username.collectAsState()
    Log.d("ViewModelCheck", "MainActivity: ${viewModel.hashCode()}")
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Unauthenticated -> {
                items = listOf(
                    BottomNavItem.SignIn,
                    BottomNavItem.SignUp
                )
                navController.navigate(Routes.SignIn)
            }

            is AuthState.Authenticated -> {
                items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.JoinGame,
                    BottomNavItem.MatchHistory,
                )
                navController.navigate(Routes.Home)
            }

            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT)
                    .show()
            }

            else -> Unit
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chess App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSquareColor,
                    titleContentColor = Color.White
                ),
                actions = {
                    if (currentRoute != Routes.SignIn && currentRoute != Routes.SignUp) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = username,  color = Color.White, fontSize = 20.sp)
                            IconButton(onClick = {
                                viewModel.signOut() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ExitToApp,
                                    tint = Color.White,
                                    contentDescription = "Sign out"
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (!currentRoute.equals(Routes.Game)) {
                NavigationBar(
                    containerColor = LightSquareColor,
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                navController.navigate(item.screenRoute) {
                                    navController.graph.startDestinationRoute?.let { screenRoute ->
                                        popUpTo(screenRoute) {
                                            saveState =
                                                true
                                        }
                                    }
                                    launchSingleTop =
                                        true
                                    restoreState =
                                        true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.LightGray,
                                selectedTextColor = Color.White,
                                unselectedTextColor = Color.LightGray,
                                indicatorColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Navigation(navController = navController, innerPadding = innerPadding, viewModel)
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: UserViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SignIn,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Routes.SignIn) { SignInScreen(viewModel) }
        composable(Routes.SignUp) { SignUpScreen(viewModel) }
        composable(Routes.Game) { navBackStackEntry ->
            val side = navBackStackEntry.arguments?.getString("side")
            val id = navBackStackEntry.arguments?.getString("id")
            if (side != null && id != null) {
                GameScreen(navController, side, id)
            }
        }
        composable(Routes.Home) { HomeScreen(navController, viewModel) }
        composable(Routes.JoinGame) { JoinGameScreen(navController, viewModel) }
        composable(Routes.MatchHistory) { MatchHistoryScreen(viewModel) }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ChessTheme {
        MainScreen(rememberNavController())
    }

}