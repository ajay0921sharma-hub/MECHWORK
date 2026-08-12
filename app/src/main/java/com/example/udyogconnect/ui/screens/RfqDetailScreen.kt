package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udyogconnect.data.local.QuoteEntity
import com.example.udyogconnect.data.local.RfqEntity
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.data.model.UserRole
import com.example.udyogconnect.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RfqDetailScreen(
    rfq: RfqEntity?,
    quotes: List<QuoteEntity>,
    currentRole: UserRole,
    language: Language,
    onBack: () -> Unit,
    onSubmitQuote: (String) -> Unit,
    onAcceptQuote: (QuoteEntity, RfqEntity) -> Unit,
    onOpenChat: (String) -> Unit
) {
    if (rfq == null) {
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
                        text = rfq.partName,
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
                    IconButton(onClick = { onOpenChat(rfq.id) }) {
                        Icon(imageVector = Icons.Outlined.Chat, contentDescription = "Chat", tint = IndustrialNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (currentRole == UserRole.SUPPLIER && rfq.status == "OPEN") {
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Button(
                            onClick = { onSubmitQuote(rfq.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SafetyOrange),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.RequestQuote, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == Language.HINDI) "कोटेशन प्रस्तुत करें" else "Submit Quotation",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
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
            // RFQ Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = IndustrialNavy.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = rfq.category,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = IndustrialNavy,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = rfq.partName,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IndustrialNavy
                                )
                            )
                            Text(
                                text = "Posted by ${rfq.buyerCompany} (${rfq.buyerName})",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Surface(
                            color = if (rfq.status == "OPEN") VerifiedGreenBg else PendingAmberBg,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = rfq.status,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (rfq.status == "OPEN") VerifiedGreen else PendingAmber,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = CardBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Specs Grid
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = "Material Grade:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = rfq.material, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                        }
                        Column {
                            Text(text = "Process:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = rfq.processRequired, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                        }
                        Column {
                            Text(text = "Tolerance:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = rfq.tolerance, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialBlue))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = "Dimensions:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = rfq.dimensions, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                        }
                        Column {
                            Text(text = "Quantity:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = "${rfq.quantity} Pcs", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = SafetyOrange))
                        }
                        Column {
                            Text(text = "Target Price:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = "₹${rfq.targetUnitPrice} / pc", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = VerifiedGreen))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Drawing Box
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFEF2F2))
                            .padding(10.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.PictureAsPdf, contentDescription = null, tint = Color(0xFFDC2626))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = rfq.drawingFileName, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                            Text(text = "CAD Spec Sheet & Tolerance Notes attached", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                        }
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = "Download", tint = IndustrialNavy)
                        }
                    }

                    if (rfq.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Quality Notes:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextMuted))
                        Text(text = rfq.notes, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quotations Side-by-Side Comparison Section
            Text(
                text = "Received Quotations & Side-by-Side Comparison (${quotes.size})",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = IndustrialNavy
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (quotes.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Outlined.HourglassEmpty, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "No quotations submitted yet for this RFQ.", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
                        }
                    }
                }
            } else {
                // Horizontal scrollable side-by-side comparison cards
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    quotes.forEach { quote ->
                        Card(
                            modifier = Modifier
                                .width(290.dp)
                                .border(
                                    width = if (quote.status == "ACCEPTED") 2.dp else 1.dp,
                                    color = if (quote.status == "ACCEPTED") VerifiedGreen else CardBorder,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    MatchScoreChip(score = quote.matchScore)
                                    VerificationBadge(statusString = quote.verificationStatus)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = quote.supplierCompany,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                                )
                                Text(
                                    text = quote.supplierLocation,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = CardBorder)
                                Spacer(modifier = Modifier.height(10.dp))

                                // Commercial Breakdown Table Rows
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "Unit Price:", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                                    Text(text = "₹${quote.unitPrice} / pc", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = VerifiedGreen))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "Tooling Cost:", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                                    Text(text = "₹${quote.toolingCost}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "GST + Freight:", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                                    Text(text = "${quote.gstPercentage}% GST + ₹${quote.freightCost}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "Total Landed Value:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                                    Text(text = "₹${String.format("%,.0f", quote.totalPrice)}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = CardBorder)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "Lead Time:", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                                    Text(text = "${quote.leadTimeDays} Days", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = IndustrialBlue))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Payment: ${quote.paymentTerms}", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))

                                Spacer(modifier = Modifier.height(12.dp))

                                // Actions for Buyer
                                if (currentRole == UserRole.BUYER) {
                                    if (quote.status == "ACCEPTED") {
                                        Surface(
                                            color = VerifiedGreenBg,
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(text = "QUOTE ACCEPTED & PO ISSUED", style = MaterialTheme.typography.labelMedium.copy(color = VerifiedGreen, fontWeight = FontWeight.Bold))
                                            }
                                        }
                                    } else {
                                        Button(
                                            onClick = { onAcceptQuote(quote, rfq) },
                                            colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(text = "Accept Quote & Issue PO", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
