package com.example.fooddelivery.authentication

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.fooddelivery.ui.FoodViewModel

@Composable
fun OtpScreen(viewModel: FoodViewModel) {

    val otp by viewModel.otp.collectAsState()
    val verificationId by viewModel.verificationId.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        Text("Enter OTP", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))

        OtpTextField(
            otp = otp,
            onOtpChange = { viewModel.setOtp(it) }
        )

        Spacer(modifier = Modifier.height(30.dp))

        androidx.compose.material3.Button(
            onClick = {
                if (otp.length == 6) {
                    val credential = com.google.firebase.auth.PhoneAuthProvider.getCredential(
                        verificationId,
                        otp
                    )
                    com.example.fooddelivery.auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                android.util.Log.d("OTP_DEBUG", "Login Success ✅")
                                viewModel.user.value = com.example.fooddelivery.auth.currentUser
                            } else {
                                android.util.Log.e(
                                    "OTP_DEBUG",
                                    "Verification Failed",
                                    task.exception
                                )
                                android.widget.Toast.makeText(
                                    context,
                                    "OTP Verification Failed: ${task.exception?.message}",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    android.widget.Toast.makeText(
                        context,
                        "Enter 6 digit OTP",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
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
        Modifier.fillMaxSize(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    ) {
        // ahiya compsury text battavu j pade baki kai show nahi thai

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
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
                        ).padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = char.toString(), fontSize = 20.sp)

                }

            }

        }

    }

}