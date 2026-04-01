package com.example.fooddelivery.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fooddelivery.FireRealDb

@Composable
fun PersonalInfoForm(viewModel: FoodViewModel, navController: NavController) {
    val firstName by viewModel.UserFirstName.collectAsState()
    val lastName by viewModel.UserlastName.collectAsState()
    val DabRef = FireRealDb.reference
 val contextt = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(modifier = Modifier.padding(15.dp)) {

            // First Name
            Text("First Name :", fontSize = 16.sp, color = TextDark)
            Spacer(Modifier.height(6.dp))
            BasicTextField(
                value = firstName,
                onValueChange = { viewModel.setUserfirstName(it) },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 15.sp, color = TextDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = OrangePrimary,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                    .padding(bottom = 6.dp),
                decorationBox = { inner ->
                    if (firstName.isEmpty()) {
                        Text("Enter first name", fontSize = 15.sp, color = TextMedium)
                    } else inner()
                })

            Spacer(Modifier.height(20.dp))

            // Last Name
            Text("Last Name :", fontSize = 16.sp, color = TextDark)
            Spacer(Modifier.height(6.dp))
            BasicTextField(
                value = lastName,
                onValueChange = { viewModel.setUserLastName(it) },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 15.sp, color = TextDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = OrangePrimary,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                    .padding(bottom = 6.dp),
                decorationBox = { inner ->
                    if (lastName.isEmpty()) {
                        Text("Enter last name", fontSize = 15.sp, color = TextMedium)
                    } else inner()
                })

            Spacer(Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    DabRef.child("Users").child("firstName").setValue(firstName)
                    DabRef.child("Users").child("lastName").setValue(lastName)
                    Toast.makeText(contextt, "Saved!", Toast.LENGTH_SHORT).show()
                   navController.popBackStack()
                },
                enabled = !firstName.isEmpty()&&!lastName.isEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
            ) {
                Text("Save", color = Color.White, fontSize = 15.sp)
            }
        }
    }
}
