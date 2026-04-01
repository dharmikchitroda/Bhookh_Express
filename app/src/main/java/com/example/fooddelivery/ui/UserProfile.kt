package com.example.fooddelivery.ui

import android.R
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.FireRealDb
import com.example.fooddelivery.auth

// ─── Orange Theme Colors ───────────────────────────────────────
val OrangePrimary = Color(0xFFFF6B00)
val OrangeLight = Color(0xFFFF8C42)
val OrangeBackground = Color(0xFFFFF8F4)
val OrangeSurface = Color(0xFFFFECDE)
val TextDark = Color(0xFF1A1A1A)
val TextMedium = Color(0xFF6B6B6B)

// ─── Data Class ───────────────────────────────────────────────
data class ProfileOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

// ─── Options List ─────────────────────────────────────────────
val options = listOf(
    ProfileOption("Personal Info", "Update your name, email & phone", Icons.Outlined.Person),
    ProfileOption("Saved Addresses", "Manage your delivery locations", Icons.Outlined.LocationOn),
    ProfileOption("Chat With whatsapp", "Manage alerts & offers", Icons.Outlined.Email),
    ProfileOption("Help & Support", "are you have any qustion....", Icons.Outlined.Call),
    ProfileOption("Logout", "Sign out of your account", Icons.Outlined.ExitToApp)
)

// ─── Main Screen ──────────────────────────────────────────────
@Composable
fun UserProfile(viewModel: FoodViewModel, navigatefunction: () -> Unit) {

    val context = LocalContext.current
    val imageUri by viewModel.UserImage.collectAsState()
    val DialogStatus by viewModel.DialogStatus.collectAsState()
    val username by viewModel.UserName.collectAsState()
    var DbRef = FireRealDb.reference
    val firstName by  viewModel.UserFirstName.collectAsState()
    val lastName by viewModel.UserlastName.collectAsState()


    if (DialogStatus) {
        SetAlert(
            onDismiss = { viewModel.setDialogStatus(false) },
            onConfirm = {
                auth.signOut()
                viewModel.ClearUser()
                viewModel.setDialogStatus(false)
            },
            title = "Logout",
            text = "Are you sure you want to logout?"
        )
    }
    val luncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            viewModel.setImage(uri)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // ── Header ──
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(OrangePrimary, OrangeLight)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (imageUri != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUri),
                                        contentDescription = null, contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Avatar",
                                        modifier = Modifier.size(60.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .align(Alignment.BottomEnd)
                                    .background(Color.White.copy(alpha = 0.25f))
                                    .clickable {
                                        luncher.launch("image/*")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Avatar",
                                    modifier = Modifier,
                                    tint = Color.Black,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "${firstName+" "+lastName}",
                            fontSize = 20.sp,
                            color = Color.White,

                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "dharmik@example.com",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // ── All Options ──
            items(options) { item ->
                ProfileItem(
                    item,
                    proonclick = { selectedItem ->
                        if (selectedItem.title == "Logout") {
                            viewModel.setDialogStatus(true)
                            //move above beacuse we will using dilog box throw take action
                            //   auth.signOut()
                            //   viewModel.ClearUser()
                        }
                        if (selectedItem.title == "Help & Support") {
                            OpenDialer(context as Activity)
                        }
                        if (selectedItem.title == "Chat With whatsapp") {
                            OpenWhatsapp(context as Activity)
                        }
                        if (selectedItem.title == "Personal Info") {
                            navigatefunction()
                        }
                    })
            }
        }
    }
}

// ─── Profile Item ─────────────────────────────────────────────
@Composable
fun ProfileItem(item: ProfileOption, proonclick: (ProfileOption) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable {
                proonclick(item)
            },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon bubble
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(OrangeSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = OrangePrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Title + subtitle
            Column(modifier = Modifier.weight(1f)) {  // ✅ weight ab kaam karega
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 12.sp,
                    color = TextMedium
                )
            }
        }
    }
}

fun OpenDialer(activity: Activity) {
    val Dialintent = Intent().apply {
        action = Intent.ACTION_DIAL
        addCategory(Intent.CATEGORY_DEFAULT)
        data = Uri.parse("tel:9876543210")
    }
    activity.startActivity(Dialintent)
}

fun OpenWhatsapp(activity: Activity) {
    val Whatsappintent = Intent().apply {
        action = Intent.ACTION_VIEW
        addCategory(Intent.CATEGORY_DEFAULT)
        data = Uri.parse("https://api.whatsapp.com/send?phone=9876543210")

        // only WhatsApp open
        setPackage("com.whatsapp")
    }
    activity.startActivity(Whatsappintent)
}

@Composable
fun SetAlert(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: String
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = Color.White,
        icon = {
            Icon(
                imageVector = Icons.Outlined.ExitToApp,
                contentDescription = null,
                tint = OrangePrimary,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(title, fontWeight = FontWeight.Medium)
        },
        text = {
            Text(text, color = TextMedium)
        },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Yes, Logout")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismiss() },
                border = BorderStroke(1.5.dp, OrangePrimary),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = OrangePrimary)
            ) {
                Text("Cancel")
            }
        }
    )
}