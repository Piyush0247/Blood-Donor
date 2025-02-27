package jain.piyush.blooddonor.navigation

sealed class Screen(val route:String) {
    data object LoginActivity:Screen("login_activity")
    data object SignupActivity:Screen("signup_activity")
    data object Dashboard:Screen("dashboard")
    data object CreateProfile:Screen("create_profile")
    data object BloodGroupScreen:Screen("blood_group_screen")
    data object DonorCompleted:Screen("donor_completed")

}