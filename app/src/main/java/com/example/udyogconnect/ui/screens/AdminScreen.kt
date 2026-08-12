package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.udyogconnect.data.local.SupplierProfileEntity
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.data.model.VerificationStatus
import com.example.udyogconnect.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    suppliers: List<SupplierProfileEntity>,
    language: Language,
    onApproveVerification: (String, VerificationStatus) -> Unit
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
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "प्रशासक नियंत्रण एवं सत्यापन केंद्र" else "UdyogConnect Admin Moderation Console",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                )
                Text(
                    text = if (language == Language.HINDI) "जीएसटी एवं एमएसएमई पंजीकरण सत्यापन और सुरक्षा जांच" else "Verify GST & MSME registrations, moderate RFQs, and oversee orders",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Supplier GST & MSME Verification Queue",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
            )
            Spacer(modifier = Modifier.height(8.dp))

            suppliers.forEach { supplier ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = supplier.companyName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                                Text(text = "City: ${supplier.city}, ${supplier.state}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                            }
                            VerificationBadge(statusString = supplier.verificationStatus)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "GSTIN: ${supplier.gstNumber}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        Text(text = "Udyam Reg: ${supplier.msmeUdyamNumber}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = VerifiedGreen))

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = CardBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Admin Action:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { onApproveVerification(supplier.supplierId, VerificationStatus.VERIFIED_GST_MSME) },
                                    colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Approve GST & MSME", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
