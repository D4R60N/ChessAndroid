package uhk.palecek.chess

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.uhk.fim.cryptoapp.networkModule
import cz.uhk.fim.cryptoapp.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin
import uhk.palecek.chess.ui.theme.ChessTheme
import uhk.palecek.chess.consts.BottomNavItem;
import uhk.palecek.chess.consts.Routes;
import uhk.palecek.chess.screens.GameScreen
import uhk.palecek.chess.screens.SignInScreen
import uhk.palecek.chess.screens.SignUpScreen
import uhk.palecek.chess.viewmodels.AuthState
import uhk.palecek.chess.viewmodels.UserViewModel
import java.net.URISyntaxException

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        try
//        {
//            val mSocket: Socket = IO.socket("localhost:8080")
//            mSocket.connect()
//        } catch (e: URISyntaxException)
//        {
//            println(e)
//        }
        startKoin {
            androidContext(this@MainActivity)
            modules(
                viewModelModule,
                networkModule,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: UserViewModel = koinViewModel()) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(0) }
    var items by remember { mutableStateOf(listOf<BottomNavItem>()) }
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
                items = listOf(BottomNavItem.Game)
                navController.navigate(Routes.Game)
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
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
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    if (navBackStackEntry != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Back"
                            )

                        }
                    }
                },
                actions = {
                    if (currentRoute != Routes.Settings) {
                        IconButton(onClick = { navController.navigate(Routes.Settings) }) {
                            Icon(
                                Icons.Filled.Settings,
                                tint = Color.White,
                                contentDescription = "Settings"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
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
    ) { innerPadding ->
        Navigation(navController = navController, innerPadding = innerPadding, viewModel)
    }
}

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues, viewModel: UserViewModel) {
    NavHost(
        navController = navController,
        startDestination = Routes.SignIn,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Routes.SignIn) { SignInScreen(navController, viewModel) }
        composable(Routes.SignUp) { SignUpScreen(navController, viewModel) }
        composable(Routes.Game) { GameScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ChessTheme {
        MainScreen(rememberNavController())
    }

}