package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udyogconnect.data.model.Language
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RfqCreateScreen(
    language: Language,
    onBack: () -> Unit,
    onPostRfq: (
        partName: String,
        category: String,
        material: String,
        processRequired: String,
        dimensions: String,
        tolerance: String,
        quantity: Int,
        targetUnitPrice: Double,
        finishCoating: String,
        deliveryDate: String,
        drawingFileName: String,
        notes: String,
        locationPref: String
    ) -> Unit
) {
    var partName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("CNC Machining & Turning") }
    var material by remember { mutableStateOf("Aluminum 6061-T6") }
    var processRequired by remember { mutableStateOf("CNC Machining") }
    var dimensions by remember { mutableStateOf("150mm x 100mm x 35mm") }
    var tolerance by remember { mutableStateOf("±0.01 mm") }
    var quantityText by remember { mutableStateOf("1000") }
    var targetUnitPriceText by remember { mutableStateOf("350.0") }
    var finishCoating by remember { mutableStateOf("Black Anodizing") }
    var deliveryDate by remember { mutableStateOf("25 Sep 2026") }
    var drawingFileName by remember { mutableStateOf("Part_Assembly_Drawing_V1.pdf") }
    var notes by remember { mutableStateOf("Requires 100% inspection report and MTC.") }
    var locationPref by remember { mutableStateOf("Rajkot, Faridabad, Pune") }

    val categories = listOf(
        "CNC Machining & Turning",
        "Sheet Metal & Laser Cutting",
        "Tooling, Dies & Molds",
        "Gears, Shafts & Forging",
        "Casting & Foundry Work",
        "Plastic Injection Molding"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (language == Language.HINDI) "नया RFQ (मांग पत्र) भेजें" else "Post Industrial RFQ Requirement",
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Part Specifications & Category",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = partName,
                        onValueChange = { partName = it },
                        label = { Text("Part Name / Component Title *") },
                        placeholder = { Text("e.g. VMC Machined Hydraulic Valve Block") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Category:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                    Spacer(modifier = Modifier.height(4.dp))
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        selectedCategory = cat
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = material,
                            onValueChange = { material = it },
                            label = { Text("Material Grade *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                        OutlinedTextField(
                            value = processRequired,
                            onValueChange = { processRequired = it },
                            label = { Text("Process Required *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = dimensions,
                            onValueChange = { dimensions = it },
                            label = { Text("Dimensions *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                        OutlinedTextField(
                            value = tolerance,
                            onValueChange = { tolerance = it },
                            label = { Text("Precision Tolerance *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "2. Commercials & Quantities",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = quantityText,
                            onValueChange = { quantityText = it },
                            label = { Text("Quantity (Pcs) *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                        OutlinedTextField(
                            value = targetUnitPriceText,
                            onValueChange = { targetUnitPriceText = it },
                            label = { Text("Target Unit Price (₹)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = finishCoating,
                            onValueChange = { finishCoating = it },
                            label = { Text("Surface Finish") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                        OutlinedTextField(
                            value = deliveryDate,
                            onValueChange = { deliveryDate = it },
                            label = { Text("Target Delivery Date") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "3. Drawings & Technical Documents",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Simulated File Upload Attachment
                    OutlinedButton(
                        onClick = { drawingFileName = "Uploaded_CAD_2026_Part.pdf" },
                        modifier = Modifier.fillMaxWidth(),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SafetyOrange),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.CloudUpload, contentDescription = null, tint = SafetyOrange)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Attached: $drawingFileName",
                            style = MaterialTheme.typography.labelMedium.copy(color = SafetyOrange, fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = locationPref,
                        onValueChange = { locationPref = it },
                        label = { Text("Preferred MSME Locations / Cities") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Quality & Testing Requirements / Notes") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val qty = quantityText.toIntOrNull() ?: 500
                            val targetPrice = targetUnitPriceText.toDoubleOrNull() ?: 300.0
                            val name = if (partName.isBlank()) "Precision Industrial Component" else partName
                            onPostRfq(
                                name,
                                selectedCategory,
                                material,
                                processRequired,
                                dimensions,
                                tolerance,
                                qty,
                                targetPrice,
                                finishCoating,
                                deliveryDate,
                                drawingFileName,
                                notes,
                                locationPref
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
                            text = if (language == Language.HINDI) "RFQ प्रकाशित करें एवं सप्लायर मैच देखें" else "Post RFQ & Match Suitable MSMEs",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
