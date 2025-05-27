package com.app1.personalpocketdictionary.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app1.personalpocketdictionary.data.DictionaryApplication
import com.app1.personalpocketdictionary.data.DictionaryViewModel
import com.app1.personalpocketdictionary.data.DictionaryViewModelFactory
import com.app1.personalpocketdictionary.ui.addandedit.AddAndEditScreen
import com.app1.personalpocketdictionary.ui.devnotes.DevNotesScreen
import com.app1.personalpocketdictionary.ui.itemdetail.ItemDetailScreen
import com.app1.personalpocketdictionary.ui.itemlist.ItemListScreen
import androidx.compose.ui.platform.LocalContext

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
    // Obtain the application instance from LocalContext
    val application = LocalContext.current.applicationContext as DictionaryApplication
    // Create the ViewModel using the factory
    val dictionaryViewModel: DictionaryViewModel = viewModel(
        factory = DictionaryViewModelFactory(application.database.getDao())
    )

    NavHost(navController = navController, startDestination = Screen.ItemList.route) {
        composable(Screen.ItemList.route) {
            ItemListScreen(viewModel = dictionaryViewModel, navController = navController)
        }
        composable(
            Screen.ItemDetail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")
            if (itemId != null) {
                ItemDetailScreen(viewModel = dictionaryViewModel, itemId = itemId, navController = navController)
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
            AddAndEditScreen(viewModel = dictionaryViewModel, navController = navController, itemId = itemId)
        }
        composable(Screen.DevNotes.route) {
            DevNotesScreen(
                onVenmoClick = onVenmoClick,
                onPaypalClick = onPaypalClick,
                onEmailClick = onEmailClick
                // If DevNotesScreen needed navController, it would be passed here
            )
        }
    }
}
