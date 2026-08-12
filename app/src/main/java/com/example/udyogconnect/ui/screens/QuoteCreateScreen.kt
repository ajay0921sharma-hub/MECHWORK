package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.udyogconnect.data.local.RfqEntity
import com.example.udyogconnect.data.model.Language
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteCreateScreen(
    rfq: RfqEntity?,
    language: Language,
    onBack: () -> Unit,
    onSubmitQuote: (
        rfqId: String,
        unitPrice: Double,
        toolingCost: Double,
        moq: Int,
        gstPercent: Double,
        freightCost: Double,
        leadTimeDays: Int,
        paymentTerms: String,
        notes: String
    ) -> Unit
) {
    if (rfq == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = SafetyOrange)
        }
        return
    }

    var unitPriceText by remember { mutableStateOf((rfq.targetUnitPrice * 0.95).toString()) }
    var toolingCostText by remember { mutableStateOf("2500.0") }
    var moqText by remember { mutableStateOf("200") }
    var gstPercentText by remember { mutableStateOf("18.0") }
    var freightCostText by remember { mutableStateOf("12.0") }
    var leadTimeDaysText by remember { mutableStateOf("12") }
    var paymentTerms by remember { mutableStateOf("30% Advance, 70% against Invoice & Dispatch") }
    var notes by remember { mutableStateOf("Dedicated VMC machines reserved. 100% CMM report & Material Test Cert (MTC) included.") }

    val unitPrice = unitPriceText.toDoubleOrNull() ?: rfq.targetUnitPrice
    val tooling = toolingCostText.toDoubleOrNull() ?: 0.0
    val freight = freightCostText.toDoubleOrNull() ?: 0.0
    val gst = gstPercentText.toDoubleOrNull() ?: 18.0

    val subtotal = (unitPrice * rfq.quantity) + tooling + freight
    val totalWithGst = subtotal * (1 + (gst / 100))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Submit Quotation / दर-सूची",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
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
            // Target Specs Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "RFQ: ${rfq.partName}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                    Text(text = "Buyer: ${rfq.buyerCompany} | Qty: ${rfq.quantity} pcs", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Text(text = "Target Price: ₹${rfq.targetUnitPrice} / pc | Specs: ${rfq.material}, ${rfq.tolerance}", style = MaterialTheme.typography.labelSmall.copy(color = VerifiedGreen))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quotation Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "1. Pricing & Tooling Breakdown", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = unitPriceText,
                            onValueChange = { unitPriceText = it },
                            label = { Text("Unit Price (₹/pc) *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                        OutlinedTextField(
                            value = toolingCostText,
                            onValueChange = { toolingCostText = it },
                            label = { Text("Tooling/Fixture (₹)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = gstPercentText,
                            onValueChange = { gstPercentText = it },
                            label = { Text("GST Rate (%)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                        OutlinedTextField(
                            value = freightCostText,
                            onValueChange = { freightCostText = it },
                            label = { Text("Freight Cost (₹)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = moqText,
                            onValueChange = { moqText = it },
                            label = { Text("MOQ (Pcs)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                        OutlinedTextField(
                            value = leadTimeDaysText,
                            onValueChange = { leadTimeDaysText = it },
                            label = { Text("Lead Time (Days)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = paymentTerms,
                        onValueChange = { paymentTerms = it },
                        label = { Text("Payment Terms *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Technical Remarks & Machines Reserved") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Calculation Summary Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = IndustrialNavy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Total Landed Order Value (With GST):", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                            Text(text = "₹${String.format("%,.2f", totalWithGst)}", style = MaterialTheme.typography.headlineSmall.copy(color = SafetyOrange, fontWeight = FontWeight.Bold))
                            Text(text = "Includes ${rfq.quantity} pcs @ ₹$unitPrice/pc + ₹$tooling Tooling + $gst% GST", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val moq = moqText.toIntOrNull() ?: 100
                            val leadTime = leadTimeDaysText.toIntOrNull() ?: 14
                            onSubmitQuote(
                                rfq.id,
                                unitPrice,
                                tooling,
                                moq,
                                gst,
                                freight,
                                leadTime,
                                paymentTerms,
                                notes
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SafetyOrange),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.HINDI) "दर-सूची भेजें" else "Submit Quotation To Buyer",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
