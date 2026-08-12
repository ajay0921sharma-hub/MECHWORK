package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udyogconnect.data.local.OrderEntity
import com.example.udyogconnect.data.local.RfqEntity
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.data.model.UserRole
import com.example.udyogconnect.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    currentRole: UserRole,
    language: Language,
    rfqs: List<RfqEntity>,
    orders: List<OrderEntity>,
    onSelectRfq: (String) -> Unit,
    onSelectOrder: (String) -> Unit,
    onNavigatePostRfq: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceGray)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Top Header
        Surface(
            color = Color.White,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = when (currentRole) {
                        UserRole.SUPPLIER -> if (language == Language.HINDI) "निर्माता डैशबोर्ड (मशीन वर्कशॉप)" else "Manufacturer & Workshop Dashboard"
                        UserRole.BUYER -> if (language == Language.HINDI) "खरीदार/कंपनी डैशबोर्ड (OEM)" else "OEM Procurement Dashboard"
                        UserRole.ADMIN -> if (language == Language.HINDI) "एडमिन कंट्रोल सेंटर" else "UdyogConnect Admin Console"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = IndustrialNavy
                    )
                )
                Text(
                    text = if (language == Language.HINDI) "लाइव ऑर्डर ट्रैकिंग एवं व्यावसायिक प्रदर्शन" else "Real-time order tracking, RFQ pipeline & analytics",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Metrics KPI Grid
            when (currentRole) {
                UserRole.SUPPLIER -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = IndustrialNavy),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Total Order Value", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                                Text(text = "₹12.15 Lakh", style = MaterialTheme.typography.titleMedium.copy(color = SafetyOrange, fontWeight = FontWeight.Bold))
                                Text(text = "+18% this month", style = MaterialTheme.typography.labelSmall.copy(color = VerifiedGreen))
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Active RFQ Leads", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                                Text(text = "${rfqs.size} Open", style = MaterialTheme.typography.titleMedium.copy(color = IndustrialNavy, fontWeight = FontWeight.Bold))
                                Text(text = "High Match: 96%", style = MaterialTheme.typography.labelSmall.copy(color = IndustrialBlue))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Machine Capacity", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                                Text(text = "78% Utilized", style = MaterialTheme.typography.titleMedium.copy(color = VerifiedGreen, fontWeight = FontWeight.Bold))
                                Text(text = "2 VMC Slots Open", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Profile Views", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                                Text(text = "420 OEMs", style = MaterialTheme.typography.titleMedium.copy(color = IndustrialNavy, fontWeight = FontWeight.Bold))
                                Text(text = "GST Verified Badge", style = MaterialTheme.typography.labelSmall.copy(color = VerifiedGreen))
                            }
                        }
                    }
                }

                UserRole.BUYER -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = IndustrialNavy),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Active RFQs Posted", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                                Text(text = "${rfqs.size} Live", style = MaterialTheme.typography.titleMedium.copy(color = SafetyOrange, fontWeight = FontWeight.Bold))
                                Text(text = "Awaiting Quotes", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Orders in Production", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                                Text(text = "${orders.size} Active", style = MaterialTheme.typography.titleMedium.copy(color = VerifiedGreen, fontWeight = FontWeight.Bold))
                                Text(text = "Track Quality & CMM", style = MaterialTheme.typography.labelSmall.copy(color = IndustrialBlue))
                            }
                        }
                    }
                }

                UserRole.ADMIN -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = IndustrialNavy),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Platform GMV", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                                Text(text = "₹4.80 Cr", style = MaterialTheme.typography.titleMedium.copy(color = SafetyOrange, fontWeight = FontWeight.Bold))
                                Text(text = "100% Verified MSMEs", style = MaterialTheme.typography.labelSmall.copy(color = VerifiedGreen))
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Pending Verifications", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                                Text(text = "14 MSMEs", style = MaterialTheme.typography.titleMedium.copy(color = PendingAmber, fontWeight = FontWeight.Bold))
                                Text(text = "GST & Udyam Queue", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active Production Orders Section
            SectionHeader(
                titleEn = "Active Production Orders & Flow",
                titleHi = "सक्रिय निर्माण आदेश",
                language = language
            )

            if (orders.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(text = "No active orders. Accept a quotation to start an order.", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
                    }
                }
            } else {
                orders.forEach { ord ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clickable { onSelectOrder(ord.id) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = ord.partName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                                    Text(text = "PO #: ${ord.poNumber} • ${ord.supplierCompany}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                                }
                                Surface(
                                    color = VerifiedGreenBg,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = ord.status,
                                        style = MaterialTheme.typography.labelSmall.copy(color = VerifiedGreen, fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = CardBorder)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Total Value: ₹${String.format("%,.0f", ord.totalAmount)}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SafetyOrange))
                                OutlinedButton(
                                    onClick = { onSelectOrder(ord.id) },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "Track Order Flow", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active RFQs Pipeline
            SectionHeader(
                titleEn = "Live RFQ Pipeline",
                titleHi = "लाइव RFQ सूची",
                language = language
            )

            rfqs.forEach { rfq ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clickable { onSelectRfq(rfq.id) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = rfq.partName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                            Text(text = rfq.status, style = MaterialTheme.typography.labelSmall.copy(color = SafetyOrange, fontWeight = FontWeight.Bold))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Qty: ${rfq.quantity} pcs | Material: ${rfq.material}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                }
            }
        }
    }
}
