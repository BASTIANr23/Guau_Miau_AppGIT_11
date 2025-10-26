package cl.duoc.guaumiau.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.guaumiau.ui.screens.catalog.CatalogScreen
import cl.duoc.guaumiau.ui.screens.catalog.CatalogViewModel
import cl.duoc.guaumiau.ui.screens.catalog.ProductDetailScreen
import cl.duoc.guaumiau.ui.screens.cart.CartScreen
import cl.duoc.guaumiau.ui.screens.cart.CartViewModel
import cl.duoc.guaumiau.ui.screens.home.HomeScreen
import cl.duoc.guaumiau.ui.screens.home.HomeViewModel
import cl.duoc.guaumiau.ui.screens.login.LoginScreen
import cl.duoc.guaumiau.ui.screens.login.LoginViewModel
import cl.duoc.guaumiau.ui.screens.pets.PetsRegistrationScreen
import cl.duoc.guaumiau.ui.screens.pets.PetsViewModel
import cl.duoc.guaumiau.ui.screens.pets.EditPetScreen
import cl.duoc.guaumiau.ui.screens.pets.EditPetViewModel
import cl.duoc.guaumiau.ui.screens.pets.PetsManagementScreen
import cl.duoc.guaumiau.ui.screens.pets.PetsManagementViewModel
import cl.duoc.guaumiau.ui.screens.pets.AddPetScreen
import cl.duoc.guaumiau.ui.screens.pets.AddPetViewModel
import cl.duoc.guaumiau.ui.screens.profile.ProfileScreen
import cl.duoc.guaumiau.ui.screens.profile.ProfileViewModel
import cl.duoc.guaumiau.ui.screens.register.RegisterScreen
import cl.duoc.guaumiau.ui.screens.register.RegisterViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()
    val isLoggedIn by homeViewModel.isLoggedIn.collectAsState(initial = false)
    
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Catalog.route else Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToCatalog = { 
                    navController.navigate(Screen.Catalog.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = viewModel()
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToPets = { navController.navigate(Screen.PetsRegistration.route) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.PetsRegistration.route) {
            val viewModel: PetsViewModel = viewModel()
            PetsRegistrationScreen(
                viewModel = viewModel,
                onNavigateToCatalog = { 
                    navController.navigate(Screen.Catalog.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Catalog.route) {
            val viewModel: CatalogViewModel = viewModel()
            CatalogScreen(
                viewModel = viewModel,
                onNavigateToDetail = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToCart = { navController.navigate(Screen.Cart.route) },
                onNavigateToPetsManagement = { navController.navigate(Screen.PetsManagement.route) }
            )
        }
        
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            val catalogViewModel: CatalogViewModel = viewModel()
            val cartViewModel: CartViewModel = viewModel()
            ProductDetailScreen(
                productId = productId,
                viewModel = catalogViewModel,
                cartViewModel = cartViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCart = { navController.navigate(Screen.Cart.route) }
            )
        }
        
        composable(Screen.Cart.route) {
            val viewModel: CartViewModel = viewModel()
            CartScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCheckout = { 
                    // TODO: Implementar checkout
                }
            )
        }
        
        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = viewModel()
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditPet = { petId ->
                    navController.navigate(Screen.EditPet.createRoute(petId))
                },
                onNavigateToPetsManagement = {
                    navController.navigate(Screen.PetsManagement.route)
                },
                onLogout = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.PetsManagement.route) {
            val viewModel: PetsManagementViewModel = viewModel()
            PetsManagementScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddPet = { navController.navigate(Screen.AddPet.route) },
                onNavigateToEditPet = { petId ->
                    navController.navigate(Screen.EditPet.createRoute(petId))
                }
            )
        }
        
        composable(Screen.AddPet.route) {
            val viewModel: AddPetViewModel = viewModel()
            AddPetScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onPetAdded = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.EditPet.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            val viewModel: EditPetViewModel = viewModel()
            EditPetScreen(
                petId = petId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onPetUpdated = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object Register : Screen("register")
    object PetsRegistration : Screen("pets_registration")
    object Catalog : Screen("catalog")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
    object Profile : Screen("profile")
    object EditPet : Screen("edit_pet/{petId}") {
        fun createRoute(petId: Int) = "edit_pet/$petId"
    }
    object PetsManagement : Screen("pets_management")
    object AddPet : Screen("add_pet")
    object Cart : Screen("cart")
}
