package com.example.physiotherapyapp.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DeviceConnection : Screen("device_connection")
    object DevicePlacement : Screen("device_placement")
    object ExerciseSelection : Screen("exercise_selection")
    object ExerciseVideo : Screen("exercise_video/{exerciseId}") {
        fun createRoute(exerciseId: String) = "exercise_video/$exerciseId"
    }
    object ExerciseSession : Screen("exercise_session/{exerciseId}") {
        fun createRoute(exerciseId: String) = "exercise_session/$exerciseId"
    }
    object Statistics : Screen("statistics")
    object Profile : Screen("profile")
    object Leaderboard : Screen("leaderboard")
    object Achievements : Screen("achievements")
}

