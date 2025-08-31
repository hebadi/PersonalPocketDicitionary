package com.app1.personalpocketdictionary.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app1.personalpocketdictionary.presentation.viewmodel.ModernDictionaryViewModel
import com.app1.personalpocketdictionary.ui.addandedit.AddAndEditScreen
import com.app1.personalpocketdictionary.ui.devnotes.DevNotesScreen
import com.app1.personalpocketdictionary.ui.itemdetail.ItemDetailScreen
import com.app1.personalpocketdictionary.ui.itemlist.ItemListScreen

sealed class Screen(val route: String) {
    object ItemList : Screen("itemList")
    object ItemDetail : Screen("itemDetail/{itemId}") {
        fun createRoute(itemId: Int) = "itemDetail/$itemId"
    }
    object AddAndEditItem : Screen("addEditItem?itemId={itemId}") { // Optional itemId
        fun createRoute(itemId: Int? = null): String {
            return if (itemId != null) "addEditItem?itemId=$itemId" else "addEditItem"
        }
    }
    object DevNotes : Screen("devNotes")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    onVenmoClick: () -> Unit,
    onPaypalClick: () -> Unit,
    onEmailClick: () -> Unit
) {
    NavHost(navController = navController, startDestination = Screen.ItemList.route) {
        composable(Screen.ItemList.route) {
            val viewModel: ModernDictionaryViewModel = hiltViewModel()
            ItemListScreen(viewModel = viewModel, navController = navController)
        }
        composable(
            Screen.ItemDetail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")
            if (itemId != null) {
                val viewModel: ModernDictionaryViewModel = hiltViewModel()
                ItemDetailScreen(
                    viewModel = viewModel,
                    itemId = itemId,
                    navController = navController
                )
            } else {
                // Handle error or navigate back
                navController.popBackStack()
            }
        }
        composable(
            Screen.AddAndEditItem.route,
            arguments = listOf(navArgument("itemId") {
                type = NavType.StringType // Navigating with Int? still means string in route
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val itemIdStr = backStackEntry.arguments?.getString("itemId")
            val itemId = itemIdStr?.toIntOrNull()
            val viewModel: ModernDictionaryViewModel = hiltViewModel()
            AddAndEditScreen(viewModel = viewModel, navController = navController, itemId = itemId)
        }
        composable(Screen.DevNotes.route) {
            DevNotesScreen(
                onVenmoClick = onVenmoClick,
                onPaypalClick = onPaypalClick,
                onEmailClick = onEmailClick,
                navController = navController
            )
        }
    }
}
