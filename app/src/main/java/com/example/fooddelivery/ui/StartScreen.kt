package com.example.fooddelivery.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.FoodDataSource
import com.example.fooddelivery.R
import kotlinx.coroutines.delay


@Composable
fun StartScreen(
    onItemsClicked: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            BannerCard()
        }
        items(FoodDataSource.foodList) { items ->

            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(Color.White),
                onClick = {
                    onItemsClicked(items.name)
                }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {

                    Text(
                        items.name, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.W500
                    )


                    Image(
                        painter = painterResource(items.img),
                        contentDescription = items.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    )
                }
            }
        }
    }
}


@Composable
fun BannerCard() {

    val images = listOf(
        R.drawable.orange_and_white_illustrative_chicken_biryani_promotion_banner,
        R.drawable.studio_shodwe_caf____bar__happy_hour_deal, R.drawable.wwww
    )

    val pagerState = rememberPagerState(
        pageCount = { images.size }
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }
    Card {
        HorizontalPager(
            state = pagerState
        ) { page ->

            Image(
                painter = painterResource(images[page]),
                contentDescription = "banner"
            )
        }
    }
}
