package com.example.fooddelivery.authentication

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.auth
import com.example.fooddelivery.ui.FoodViewModel
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import androidx.compose.ui.text.TextStyle
import java.util.concurrent.TimeUnit

@Composable
fun NumberScreen(viewModel: FoodViewModel,callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {


    val context = LocalContext.current

    val phone by viewModel.PhoneNumber.collectAsState()



    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {



            OutlinedTextField(
                value = phone,
                onValueChange = {
                    if (it.length <= 10 && it.all { c -> c.isDigit() }) {
                        viewModel.setPhoneNumber(it)
                    }
                },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                textStyle = TextStyle(fontSize = 20.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91$phone") // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(context as Activity) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    // “Firebase, OTP bhej do”
                    PhoneAuthProvider.verifyPhoneNumber(options)


                },

                shape = MaterialTheme.shapes.medium, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA726)
                ), modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {// this text for show
                Text(
                    text = "Send OTP", color = Color.White, fontSize = 16.sp
                )
            }
        }
    }
}