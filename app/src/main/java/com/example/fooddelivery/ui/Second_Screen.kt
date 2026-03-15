package com.example.fooddelivery.ui

import SecondScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.R


@Composable
fun InternetItemsScreen(viewModel: FoodViewModel) {

    when (val state = viewModel.itemUiState) {

        is FoodViewModel.ItemUiState.Loading -> {
            LoadingScreen()
        }

        is FoodViewModel.ItemUiState.Success -> {

            val filteredList = state.items.filter {
                it.categoryName == viewModel.uistate.value.MoveText // kona par click kae and shu show thashe aena mate
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(3.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredList) { item ->
                    SecondScreen(item, viewModel)
                }
            }
        }


        is FoodViewModel.ItemUiState.Error -> {
            ErrorScreen(
                message = state.message, viewModel = viewModel
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.Companion.fillMaxSize(), contentAlignment = Alignment.Companion.Center
    ) {
        Image(
            painter = painterResource(R.drawable.download), contentDescription = "Loading Image"
        )
    }
}

@Composable
fun ErrorScreen(viewModel: FoodViewModel, message: String) {
    Column(
        modifier = Modifier.Companion.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Companion.CenterHorizontally

    ) {
        Text(text = message, textAlign = TextAlign.Companion.Center)
        Spacer(modifier = Modifier.Companion.height(8.dp))
        Button(onClick = { viewModel.getItems() }) {
            Text("Try Again")

        }
    }
}