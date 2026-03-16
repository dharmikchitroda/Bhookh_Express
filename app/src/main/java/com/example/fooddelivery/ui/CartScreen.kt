package com.example.fooddelivery.ui

import InternetData
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fooddelivery.R

@Composable
fun CartScreen(
    viewModel: FoodViewModel, BrowseProducts: () -> Unit,

) {

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

        if (cartIteam.isNotEmpty()) {
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
        } else {
            EmprtCartScreen(BrowseProducts)
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

@Composable
fun EmprtCartScreen(BrowseProducts: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(R.drawable.download),
            contentDescription = "Empty Cart",
            modifier = Modifier
                .size(220.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Your cart is empty",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Looks like you haven't added anything yet",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick =  BrowseProducts ,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)
        ) {
            Text(
                text = "Browse Products",
                fontSize = 16.sp
            )
        }
    }
}