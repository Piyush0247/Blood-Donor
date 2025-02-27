package jain.piyush.blooddonor.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import jain.piyush.blooddonor.navigation.Screen
import jain.piyush.blooddonor.ui.theme.BloodDonorTheme
import jain.piyush.blooddonor.viewmodel.PersonDatabaseViewModel
import jain.piyush.blooddonor.viewmodel.ShareEmailViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BloodDonorTheme {
                val navController = rememberNavController()
                NavigationScreen(navController = navController)
            }
        }
    }
}

@Composable
fun NavigationScreen(navController: NavHostController) {
    var startDestination = "login_activity"
    val viewModel: PersonDatabaseViewModel = viewModel()
    val emailVieModel:ShareEmailViewModel = viewModel()

    FirebaseAuth.getInstance().currentUser?.let {
        startDestination = "dashboard"
    }

    NavHost(navController, startDestination = startDestination) {
        composable(Screen.LoginActivity.route) {
            LoginActivity(navController)
        }
        composable(Screen.SignupActivity.route) {
            SignUpActivity(navController)
        }
        composable(Screen.Dashboard.route) {
            DashBoard(navController, viewModel = viewModel)
        }
        composable( Screen.CreateProfile.route)
         {
            CreateProfile(navController, onNextClick =
            { navController.navigate(Screen.BloodGroupScreen.route) },
                viewModel = emailVieModel)

        }
        composable(Screen.BloodGroupScreen.route) {
            BloodGroupScreen(navController, onNextClick = { navController.navigate(Screen.Dashboard.route) })
        }
        composable(Screen.DonorCompleted.route) {
            DonorCompleted(viewModel = viewModel,navController)
        }

    }
}
