package com.example.fooddelivery


import InternetData
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.authentication.LoginScreen
import com.example.fooddelivery.ui.CartScreen
import com.example.fooddelivery.ui.FoodViewModel
import com.example.fooddelivery.ui.InternetItemsScreen
import com.example.fooddelivery.ui.ScreenName
import com.example.fooddelivery.ui.Splash
import com.example.fooddelivery.ui.StartScreen
import com.example.fooddelivery.ui.UserProfile
import com.google.firebase.auth.FirebaseAuth

data class CardData(var img: Int, var name: String)


object FoodDataSource {
    val foodList = listOf(

        CardData(R.drawable.hvww, "Fresh Fruits"),
        CardData(R.drawable.khg, "Pizza"),
        CardData(R.drawable.jgv, "Fresh Vegetables"),
        CardData(R.drawable.hvww, "Beverages"),
        CardData(R.drawable.bollsw, "Fresh Vegetables"),

        )
}

val auth = FirebaseAuth.getInstance()

@Composable
fun Main() {
    val viewmodel: FoodViewModel = viewModel()
    val state by viewmodel.uistate.collectAsState()
    val isvisible by viewmodel.IsVisible.collectAsState()
    val navController = rememberNavController()
    val cartIteam by viewmodel.emptyListFlow.collectAsState()

    //Mujhe Firebase ka login system use karna hai

    val user = viewmodel.user.collectAsState()
    // this is shoulb be null in starting
    viewmodel.setUser(auth.currentUser)


    //detect the current screen
    val BackStackEntry by navController.currentBackStackEntryAsState() // backstack mathi current backstack ni infor aapshe
    val currentRoute = BackStackEntry?.destination?.route
        ?: ScreenName.first // return first screen ho to → "first" or second screen ho to → "second"

    // for splash screen
    if (isvisible == true) {
        Splash()
    } else if (user.value == null) {
        LoginScreen(viewmodel)
    } else {

        Scaffold(topBar = {
            CustomTopBar(
                showBackArrow = currentRoute != ScreenName.first,
                onBackClick = { navController.popBackStack() },

                onUserClick = {
                    if (currentRoute != ScreenName.Fourth) {
                        navController.navigate(ScreenName.Fourth)
                    }
                },
                viewmodel
            )
        }, bottomBar = {
            CustomBottomBar(navController, currentRoute, cartIteam)
        }) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = ScreenName.first,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(ScreenName.first) {
                    //State Hoisting navigation
                    StartScreen { selectedName ->
                        viewmodel.EventChange(selectedName)
                        navController.navigate(ScreenName.second)
                    }
                }
                composable(ScreenName.second) {
                    InternetItemsScreen(viewmodel)
                }
                composable(ScreenName.Third) {
                    CartScreen(
                        viewmodel, BrowseProducts = {
                            navController.navigate(ScreenName.first)
                        }

                    )
                }
                composable(ScreenName.Fourth) {
                    UserProfile(viewmodel)
                }
            }
        }
    }
}


@Composable
fun CustomTopBar(
    showBackArrow: Boolean,
    onBackClick: () -> Unit,
    onUserClick: () -> Unit,
    viewModel: FoodViewModel
) {
    val imageUri = viewModel.UserImage.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF8200))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showBackArrow) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onBackClick() })

                Spacer(modifier = Modifier.width(8.dp))
            }



            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Bhookh Express",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row {
            Spacer(modifier = Modifier.width(12.dp))
            if (imageUri.value != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri.value),
                    contentDescription = null, contentScale = ContentScale.Crop,
                    modifier = Modifier.size(26.dp).clip(CircleShape),
                )
            } else Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User",
                tint = Color.White,
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onUserClick() })
        }
    }
}

@Composable
fun CustomBottomBar(
    navController: NavController, currentroute: String, cartIteam: List<InternetData>
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF8200))
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround

    ) {

        // Home icon
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate(ScreenName.first) }) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "Home", color = Color.White, fontSize = 12.sp
            )
        }

        // Cart icon
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {
                if (currentroute != ScreenName.Third) {
                    navController.navigate(ScreenName.Third)
                }

            },
        ) {
            Box {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                if (cartIteam.isNotEmpty()) {
                    Card(
                        modifier = Modifier.align(alignment = Alignment.TopEnd),

                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.Red)
                    ) {
                        Text(
                            cartIteam.size.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
            Text(
                text = "Cart", color = Color.White, fontSize = 12.sp
            )
        }
    }
}