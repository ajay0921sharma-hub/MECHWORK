package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.udyogconnect.data.local.RfqEntity
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.data.model.RfqStatus
import com.example.udyogconnect.data.model.UserRole
import com.example.udyogconnect.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RfqListScreen(
    rfqs: List<RfqEntity>,
    currentRole: UserRole,
    language: Language,
    onSelectRfq: (String) -> Unit,
    onPostRfq: () -> Unit,
    onSubmitQuote: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf("ALL") }

    val filteredRfqs = remember(rfqs, selectedTab) {
        when (selectedTab) {
            "OPEN" -> rfqs.filter { it.status == RfqStatus.OPEN.name }
            "IN_REVIEW" -> rfqs.filter { it.status == RfqStatus.IN_REVIEW.name }
            "AWARDED" -> rfqs.filter { it.status == RfqStatus.AWARDED.name }
            else -> rfqs
        }
    }

    Scaffold(
        floatingActionButton = {
            if (currentRole == UserRole.BUYER) {
                ExtendedFloatingActionButton(
                    onClick = onPostRfq,
                    containerColor = SafetyOrange,
                    contentColor = Color.White,
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                    text = { Text(text = if (language == Language.HINDI) "+ नया RFQ बनाएं" else "+ Post New RFQ", fontWeight = FontWeight.Bold) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SurfaceGray)
                .padding(bottom = 80.dp)
        ) {
            // Header
            Surface(
                color = Color.White,
                tonalElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == Language.HINDI) "औद्योगिक RFQ निविदाएं" else "Industrial RFQs & Requirements Portal",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = IndustrialNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == Language.HINDI) "तकनीकी ड्राइंग डाउनलोड करें एवं अपनी दरें प्रस्तुत करें" else "Browse verified OEM parts requirements, inspect drawings & submit quotations",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status Tabs
                    TabRow(
                        selectedTabIndex = when (selectedTab) {
                            "OPEN" -> 1
                            "IN_REVIEW" -> 2
                            "AWARDED" -> 3
                            else -> 0
                        },
                        containerColor = Color.White,
                        contentColor = SafetyOrange
                    ) {
                        Tab(
                            selected = selectedTab == "ALL",
                            onClick = { selectedTab = "ALL" },
                            text = { Text(text = "All RFQs (${rfqs.size})", fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == "OPEN",
                            onClick = { selectedTab = "OPEN" },
                            text = { Text(text = "Open", fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == "IN_REVIEW",
                            onClick = { selectedTab = "IN_REVIEW" },
                            text = { Text(text = "Under Review", fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredRfqs) { rfq ->
                    HighDensityRfqCard(
                        rfq = rfq,
                        language = language,
                        matchScore = 95,
                        onSelect = { onSelectRfq(rfq.id) }
                    )
                }
            }
        }
    }
}
