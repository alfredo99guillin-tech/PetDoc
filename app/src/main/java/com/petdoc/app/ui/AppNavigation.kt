package com.petdoc.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.*
import com.petdoc.app.ui.home.AddPetRoute
import com.petdoc.app.ui.home.HomeRoute
import com.petdoc.app.ui.map.MapScreen
import com.petdoc.app.ui.passport.PassportRoute

sealed class Screen(val route: String) {
    data object Home     : Screen("home")
    data object AddPet   : Screen("add_pet")
    data object Map      : Screen("map")
    data object Passport : Screen("passport/{petId}") {
        fun createRoute(petId: Int) = "passport/$petId"
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Screen.Home.route, modifier = modifier) {

        composable(Screen.Home.route) {
            HomeRoute(
                onPetClick  = { navController.navigate(Screen.Passport.createRoute(it)) },
                onAddPetClick = { navController.navigate(Screen.AddPet.route) },
                onMapClick  = { navController.navigate(Screen.Map.route) }
            )
        }

        composable(Screen.AddPet.route) {
            AddPetRoute(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Map.route) {
            MapScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            Screen.Passport.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { back ->
            val petId = back.arguments?.getInt("petId") ?: return@composable
            PassportRoute(petId = petId, onNavigateBack = { navController.popBackStack() })
        }
    }
}
