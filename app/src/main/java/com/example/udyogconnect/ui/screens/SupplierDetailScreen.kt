package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udyogconnect.data.local.ReviewEntity
import com.example.udyogconnect.data.local.SupplierProfileEntity
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SupplierDetailScreen(
    supplier: SupplierProfileEntity?,
    reviews: List<ReviewEntity>,
    language: Language,
    onBack: () -> Unit,
    onRequestQuote: (String) -> Unit,
    onOpenChat: (String) -> Unit
) {
    if (supplier == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = SafetyOrange)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = supplier.companyName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    VerificationBadge(statusString = supplier.verificationStatus)
                    Spacer(modifier = Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onOpenChat(supplier.supplierId) },
                        modifier = Modifier.weight(1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialNavy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.Chat, contentDescription = null, tint = IndustrialNavy)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Chat / Query", style = MaterialTheme.typography.labelMedium.copy(color = IndustrialNavy, fontWeight = FontWeight.Bold))
                    }

                    Button(
                        onClick = { onRequestQuote(supplier.supplierId) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SafetyOrange),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Request Quote", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SurfaceGray)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Company Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = supplier.companyName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = IndustrialNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = SafetyOrange, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${supplier.city}, ${supplier.state} (Industrial Zone)",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    RatingBar(rating = supplier.rating, reviewCount = supplier.reviewCount)

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = CardBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    // GST & MSME Government Registration Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "GST Registration:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = supplier.gstNumber, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                        }
                        Column {
                            Text(text = "MSME Udyam Reg:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = supplier.msmeUdyamNumber, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = VerifiedGreen))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Plant Capabilities & Specifications
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Plant Machine Inventory & Capabilities",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Machine List
                    Text(text = "Machines & Equipment:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = SafetyOrange))
                    Spacer(modifier = Modifier.height(6.dp))
                    val machines = supplier.machinesJson.replace("[", "").replace("]", "").replace("\"", "").split(",")
                    machines.forEach { m ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = m.trim(), style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Precision Tolerance
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Precision Tolerance:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = supplier.tolerancesJson, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialBlue))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Monthly Capacity:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = supplier.maxCapacityMonthly, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = VerifiedGreen))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Materials Handled
                    Text(text = "Materials Processed:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = SafetyOrange))
                    Spacer(modifier = Modifier.height(6.dp))
                    val materials = supplier.materialsJson.replace("[", "").replace("]", "").replace("\"", "").split(",")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        materials.forEach { mat ->
                            MachineTag(tag = mat.trim())
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Past OEM Work & Clients
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Major OEM Clients & Track Record",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val clients = supplier.pastClientsJson.replace("[", "").replace("]", "").replace("\"", "").split(",")
                    clients.forEach { c ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null, tint = SafetyOrange, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = c.trim(), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, color = TextPrimary))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Orders Delivered:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = "${supplier.totalOrdersCompleted}+ Completed", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                        }
                        Column {
                            Text(text = "Avg RFQ Response:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = "< ${supplier.responseTimeHours} Hours", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = VerifiedGreen))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // OEM Reviews
            Text(
                text = "OEM Buyer Reviews & Ratings",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (reviews.isEmpty()) {
                Text(text = "No reviews yet.", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
            } else {
                reviews.forEach { rev ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${rev.buyerName} (${rev.buyerCompany})",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                                )
                                RatingBar(rating = rev.rating, reviewCount = 0)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = rev.comment, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                    }
                }
            }
        }
    }
}
