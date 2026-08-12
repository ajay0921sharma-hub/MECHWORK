package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.udyogconnect.data.local.OrderEntity
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.data.model.OrderStatus
import com.example.udyogconnect.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderLifecycleScreen(
    order: OrderEntity?,
    currentRole: UserRole,
    language: Language,
    onBack: () -> Unit,
    onAdvanceStatus: (OrderEntity, OrderStatus, String) -> Unit,
    onSubmitReview: (supplierId: String, orderId: String, rating: Float, comment: String) -> Unit,
    onOpenChat: (String) -> Unit
) {
    if (order == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No active order selected. Select an order from Dashboard.", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
        }
        return
    }

    val currentStatus = try { OrderStatus.valueOf(order.status) } catch (e: Exception) { OrderStatus.QUOTE_ACCEPTED }

    var ratingState by remember { mutableStateOf(5.0f) }
    var reviewComment by remember { mutableStateOf("Great manufacturing precision and on-time delivery.") }
    var reviewSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Order Workflow Tracker", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text(text = "PO #: ${order.poNumber}", style = MaterialTheme.typography.labelSmall.copy(color = SafetyOrange))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onOpenChat(order.rfqId) }) {
                        Icon(imageVector = Icons.Outlined.Chat, contentDescription = "Order Chat", tint = IndustrialNavy)
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
            // Order Overview Card
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
                            Text(text = order.partName, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                            Text(text = "Buyer: ${order.buyerCompany}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                            Text(text = "Supplier: ${order.supplierCompany}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                        Surface(
                            color = SafetyOrangeBg,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "₹${String.format("%,.0f", order.totalAmount)}",
                                style = MaterialTheme.typography.titleMedium.copy(color = SafetyOrange, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = CardBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = "Quantity:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = "${order.quantity} Pcs", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                        }
                        Column {
                            Text(text = "Unit Rate:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = "₹${order.unitPrice} / pc", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = VerifiedGreen))
                        }
                        Column {
                            Text(text = "Target Delivery:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text(text = order.estimatedDeliveryDate, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialBlue))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Order Workflow Timeline Steps Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Production & Delivery Workflow",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    val allSteps = OrderStatus.values()
                    allSteps.forEachIndexed { index, step ->
                        val isDone = step.stepIndex <= currentStatus.stepIndex
                        val isCurrent = step.stepIndex == currentStatus.stepIndex

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isDone) VerifiedGreen else Color(0xFFE2E8F0)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                } else {
                                    Text(text = "${step.stepIndex}", style = MaterialTheme.typography.labelMedium.copy(color = TextMuted, fontWeight = FontWeight.Bold))
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == Language.HINDI) step.labelHi else step.labelEn,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCurrent) IndustrialNavy else if (isDone) TextPrimary else TextMuted
                                    )
                                )
                                if (isCurrent) {
                                    Text(
                                        text = order.trackingNotes,
                                        style = MaterialTheme.typography.bodySmall.copy(color = SafetyOrange, fontSize = 11.sp)
                                    )
                                }
                            }
                        }

                        if (index < allSteps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 15.dp)
                                    .width(2.dp)
                                    .height(16.dp)
                                    .background(if (step.stepIndex < currentStatus.stepIndex) VerifiedGreen else Color(0xFFE2E8F0))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Card to Advance Order State
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Update Order Status",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    when (currentStatus) {
                        OrderStatus.QUOTE_ACCEPTED -> {
                            Button(
                                onClick = { onAdvanceStatus(order, OrderStatus.PO_ISSUED, "Official Purchase Order $order.poNumber generated and sent to supplier.") },
                                colors = ButtonDefaults.buttonColors(containerColor = IndustrialNavy),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Issue Purchase Order (PO)")
                            }
                        }
                        OrderStatus.PO_ISSUED -> {
                            Button(
                                onClick = { onAdvanceStatus(order, OrderStatus.IN_PRODUCTION, "Raw material sourced. Machines set up and VMC production started.") },
                                colors = ButtonDefaults.buttonColors(containerColor = SafetyOrange),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.PrecisionManufacturing, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Start Production in Workshop")
                            }
                        }
                        OrderStatus.IN_PRODUCTION -> {
                            Button(
                                onClick = { onAdvanceStatus(order, OrderStatus.QUALITY_CHECK, "100% CMM dimensional inspection and surface roughness check completed.") },
                                colors = ButtonDefaults.buttonColors(containerColor = IndustrialBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.TaskAlt, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Upload CMM Inspection & Pass QA")
                            }
                        }
                        OrderStatus.QUALITY_CHECK -> {
                            Button(
                                onClick = { onAdvanceStatus(order, OrderStatus.DISPATCHED, "Packed in wooden crates. Dispatched via GATI Transport LR #991823.") },
                                colors = ButtonDefaults.buttonColors(containerColor = SafetyOrange),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Dispatch Shipment & Add Tracking")
                            }
                        }
                        OrderStatus.DISPATCHED -> {
                            Button(
                                onClick = { onAdvanceStatus(order, OrderStatus.DELIVERED, "Shipment received at OEM factory gate. Verified quantity.") },
                                colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.Inventory, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Mark Order Delivered at OEM Factory")
                            }
                        }
                        OrderStatus.DELIVERED -> {
                            Text(text = "Order successfully delivered! Leave a supplier review below.", style = MaterialTheme.typography.bodyMedium.copy(color = VerifiedGreen))
                        }
                        OrderStatus.COMPLETED -> {
                            Text(text = "Order completed and closed.", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
                        }
                    }
                }
            }

            // Review Submission Section if Delivered
            if (currentStatus == OrderStatus.DELIVERED || currentStatus == OrderStatus.COMPLETED) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Submit Workshop Review & Quality Rating", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndustrialNavy))
                        Spacer(modifier = Modifier.height(10.dp))

                        if (reviewSubmitted) {
                            Text(text = "Thank you! Your review has been published.", style = MaterialTheme.typography.bodyMedium.copy(color = VerifiedGreen, fontWeight = FontWeight.Bold))
                        } else {
                            OutlinedTextField(
                                value = reviewComment,
                                onValueChange = { reviewComment = it },
                                label = { Text("Quality, Precision & Timeliness Feedback") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SafetyOrange)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    onSubmitReview(order.supplierId, order.id, ratingState, reviewComment)
                                    onAdvanceStatus(order, OrderStatus.COMPLETED, "Review submitted by OEM buyer.")
                                    reviewSubmitted = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SafetyOrange),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Submit Verified Review", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
