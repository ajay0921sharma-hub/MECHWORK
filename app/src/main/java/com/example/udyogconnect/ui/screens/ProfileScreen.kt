package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.udyogconnect.data.local.UserEntity
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.data.model.UserRole
import com.example.udyogconnect.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: UserEntity?,
    currentRole: UserRole,
    language: Language,
    onRoleSelect: (UserRole) -> Unit,
    onLanguageToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceGray)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        Surface(
            color = Color.White,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(IndustrialNavy),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user?.name ?: "User Profile",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                )
                Text(
                    text = user?.companyName ?: "Manufacturing Workshop",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )

                Spacer(modifier = Modifier.height(8.dp))
                VerificationBadge(statusString = user?.verificationStatus ?: "VERIFIED_GST_MSME")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Role Switcher Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Switch User Persona", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                    Spacer(modifier = Modifier.height(8.dp))

                    UserRole.values().forEach { role ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = role == currentRole,
                                onClick = { onRoleSelect(role) },
                                colors = RadioButtonDefaults.colors(selectedColor = SafetyOrange)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == Language.HINDI) role.labelHi else role.labelEn,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Settings Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Preferences & Support", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.Translate, contentDescription = null, tint = IndustrialNavy)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "App Language / भाषा", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                        OutlinedButton(
                            onClick = onLanguageToggle,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(text = language.displayName, fontWeight = FontWeight.Bold, color = SafetyOrange)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = CardBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.HeadsetMic, contentDescription = null, tint = IndustrialNavy)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "UdyogConnect MSME Helpline", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(text = "Toll Free: 1800 200 9000 | support@udyogconnect.in", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                    }
                }
            }
        }
    }
}
