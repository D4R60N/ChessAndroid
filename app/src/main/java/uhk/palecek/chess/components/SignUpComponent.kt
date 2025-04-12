package uhk.palecek.chess.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import uhk.palecek.chess.R
import uhk.palecek.chess.viewmodels.AuthState
import uhk.palecek.chess.viewmodels.UserViewModel

@Composable
fun SignUpComponent(
    navController: NavController,
    viewModel: UserViewModel,
) {
    var username: String by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordRep by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = username,
            onValueChange = { string -> username = string },
            label = { Text("Username") },
            singleLine = true,
            placeholder = { Text("Username") }
            )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { string -> password = string },
            label = { Text("Password") },
            singleLine = true,
            placeholder = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    R.drawable.visibility
                else R.drawable.visibility_off

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(painter = painterResource(image), description)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = passwordRep,
            onValueChange = { string -> passwordRep = string },
            label = { Text("Confirm Password") },
            singleLine = true,
            placeholder = { Text("Confirm Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    R.drawable.visibility
                else R.drawable.visibility_off

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(painter = painterResource(image), description)
                }
            }
        )
        Button(
            onClick = {
                if(password.equals(passwordRep)) {
                    viewModel.signUp(username, password)
                }
                else if(password.length < 8) {
                    Toast.makeText(context, "Password needs to be at least 8 characters!", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.background(Color.Transparent),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Sign Up")
        }
    }
}
