package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.udyogconnect.data.local.SupplierProfileEntity
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierSearchScreen(
    suppliers: List<SupplierProfileEntity>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedCity: String,
    onCitySelect: (String) -> Unit,
    selectedProcess: String,
    onProcessSelect: (String) -> Unit,
    language: Language,
    onSelectSupplier: (String) -> Unit,
    onPostRfqForSupplier: (String) -> Unit
) {
    val cities = listOf("All Cities", "Rajkot", "Faridabad", "Pune", "Coimbatore", "Gurugram", "Noida", "Ahmedabad", "Ludhiana", "Delhi", "Chennai")
    val processes = listOf("All Processes", "CNC Machining", "Sheet Metal", "Tooling & Dies", "Gear Hobbing", "VMC Precision Milling", "Precision Grinding")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceGray)
            .padding(bottom = 80.dp)
    ) {
        // Search & Filter Header
        Surface(
            color = Color.White,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "सत्यापित निर्माता निर्देशिका" else "Verified MSME Manufacturers & Workshops",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = IndustrialNavy
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = if (language == Language.HINDI) "कंपनी नाम, मशीन (VMC, Laser) या शहर से खोजें..." else "Search company, machine (VMC, Laser) or city...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SafetyOrange)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SafetyOrange,
                        unfocusedBorderColor = CardBorder
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // City Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cities) { city ->
                        val isSelected = city == selectedCity
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCitySelect(city) },
                            label = {
                                Text(
                                    text = city,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = IndustrialNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Process Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(processes) { proc ->
                        val isSelected = proc == selectedProcess
                        FilterChip(
                            selected = isSelected,
                            onClick = { onProcessSelect(proc) },
                            label = {
                                Text(
                                    text = proc,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SafetyOrange,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Results Summary
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${suppliers.size} ${if (language == Language.HINDI) "निर्माता मिले" else "Workshops Found"}",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = IndustrialNavy
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sorted by Rating & Capabilities",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Supplier List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(suppliers) { supplier ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectSupplier(supplier.supplierId) },
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
                                Text(
                                    text = supplier.companyName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IndustrialNavy
                                    )
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = SafetyOrange,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${supplier.city}, ${supplier.state}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                    )
                                }
                            }
                            VerificationBadge(statusString = supplier.verificationStatus)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Machines Inventory Preview
                        Text(
                            text = "Key Machines & Plant Equipment:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        val machines = supplier.machinesJson.replace("[", "").replace("]", "").replace("\"", "").split(",")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            machines.take(3).forEach { m ->
                                MachineTag(tag = m.trim())
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Past OEM Clients
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.Business, contentDescription = null, tint = IndustrialBlue, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            val clients = supplier.pastClientsJson.replace("[", "").replace("]", "").replace("\"", "")
                            Text(
                                text = "OEM Clients: $clients",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = CardBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RatingBar(rating = supplier.rating, reviewCount = supplier.reviewCount)

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { onSelectSupplier(supplier.supplierId) },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "View Workshop", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                }

                                Button(
                                    onClick = { onPostRfqForSupplier(supplier.supplierId) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SafetyOrange),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "Request Quote", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
