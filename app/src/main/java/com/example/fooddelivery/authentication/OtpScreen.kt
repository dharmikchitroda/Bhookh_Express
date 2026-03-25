package com.example.fooddelivery.authentication

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fooddelivery.auth
import com.example.fooddelivery.ui.FoodViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@Composable
fun OtpScreen(viewModel: FoodViewModel) {

    val otp by viewModel.otp.collectAsState()
    val verificationId by viewModel.verificationId.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text("Enter OTP", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(20.dp))

            OtpTextField(
                otp = otp,
                onOtpChange = { viewModel.setOtp(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (otp.isNotEmpty()) {
                        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
                        signInWithPhoneAuthCredential(credential,context,viewModel)

                    } else {
                        Toast.makeText(context, "Please Enter 6 Digits OTP", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA726)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(text = "Verify OTP", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun OtpTextField(
    otp: String,
    onOtpChange: (String) -> Unit
) {
    BasicTextField(
        value = otp,
        onValueChange = {
            if (it.length <= 6) {
                onOtpChange(it)
            }
        },
        Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    ) {
        // ahiya compsury text battavu j pade baki kai show nahi thai

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            repeat(6) { index ->
                // aa final number give me now i can print diffrent way
                val char = when {
                    index < otp.length -> otp[index]
                    else -> ""
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            2.dp,
                            Color.Gray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = char.toString(), fontSize = 20.sp)

                }

            }

        }

    }

}


// otp verification code
private fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    context: Context,
    viewModel: FoodViewModel
) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {

                val user = task.result?.user
                viewModel.setUser(user)
                Toast.makeText(context, "Authentication Successful", Toast.LENGTH_SHORT).show()

            } else {

                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                }

            }
        }
}