package com.example.physiotherapyapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.physiotherapyapp.presentation.achievements.AchievementsScreen
import com.example.physiotherapyapp.presentation.device.DeviceConnectionScreen
import com.example.physiotherapyapp.presentation.device.DevicePlacementScreen
import com.example.physiotherapyapp.presentation.exercise.ExerciseSelectionScreen
import com.example.physiotherapyapp.presentation.exercise.ExerciseSessionScreen
import com.example.physiotherapyapp.presentation.exercise.ExerciseVideoScreen
import com.example.physiotherapyapp.presentation.home.HomeScreen
import com.example.physiotherapyapp.presentation.leaderboard.LeaderboardScreen
import com.example.physiotherapyapp.presentation.profile.ProfileScreen
import com.example.physiotherapyapp.presentation.statistics.StatisticsScreen

@Composable
fun PhysiotherapyNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.DeviceConnection.route) {
            DeviceConnectionScreen(navController = navController)
        }
        
        composable(Screen.DevicePlacement.route) {
            DevicePlacementScreen(navController = navController)
        }
        
        composable(Screen.ExerciseSelection.route) {
            ExerciseSelectionScreen(navController = navController)
        }
        
        composable(Screen.ExerciseVideo.route) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            ExerciseVideoScreen(
                exerciseId = exerciseId,
                navController = navController
            )
        }
        
        composable(Screen.ExerciseSession.route) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            ExerciseSessionScreen(
                exerciseId = exerciseId,
                navController = navController
            )
        }
        
        composable(Screen.Statistics.route) {
            StatisticsScreen(navController = navController)
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(navController = navController)
        }
        
        composable(Screen.Achievements.route) {
            AchievementsScreen(navController = navController)
        }
    }
}

