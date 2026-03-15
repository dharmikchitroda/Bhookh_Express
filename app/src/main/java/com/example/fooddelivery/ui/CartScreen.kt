package com.example.fooddelivery.ui

import InternetData
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun CartScreen(viewModel: FoodViewModel) {

    val cartIteam by viewModel.emptyListFlow.collectAsState()

    val cartItemWithQuntity = cartIteam
        .groupBy { it }
        .map { (item, list) ->
            Pair(item, list.size)
        }

    val totalPrice = cartIteam.sumOf { it.itemPrice }
    val handlingFee = if (cartIteam.isNotEmpty()) 100 else 0
    val deliveryFee = if (cartIteam.isNotEmpty()) 30 else 0
    val grandTotal = totalPrice + handlingFee + deliveryFee

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(cartItemWithQuntity) { (item, quantity) ->
                CartCard(item, viewModel, quantity)
            }

            item {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFECECEC),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {

                        BillingRow("Item Total", totalPrice, FontWeight.Normal)

                        BillingRow("Handling Charge", handlingFee, FontWeight.Light)

                        BillingRow("Delivery Fee", deliveryFee, FontWeight.Light)

                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )

                        BillingRow("To Pay", grandTotal, FontWeight.ExtraBold)
                    }
                }
            }
        }

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C4200)
            )
        ) {

            Text(
                text = "Pay $grandTotal",
                fontSize = 16.sp
            )
        }
    }
}
@Composable
fun CartCard(
    cartitem: InternetData,
    viewModel: FoodViewModel,
    quantity: Int
) {

    ElevatedCard(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(110.dp),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = cartitem.imageSource,
                contentDescription = cartitem.name,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(3f)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 4.dp)
                    .weight(3f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = cartitem.name,
                    fontSize = 20.sp,
                    maxLines = 1
                )

                Text(
                    text = "₹${cartitem.itemPrice}",
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 4.dp)
                    .weight(3f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Quantity: $quantity",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Card(
                    modifier = Modifier
                        .clickable {
                            viewModel.removeFromCart(item = cartitem)
                        }
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {

                    Text(
                        text = "Remove",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BillingRow(
    name: String,
    price: Int,
    fontWeight: FontWeight
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {

        Text(
            text = name,
            fontWeight = fontWeight,
            color = Color.Black
        )

        Text(
            text = "₹$price",
            fontWeight = fontWeight,
            color = Color.Black
        )
    }
}