package com.example.udyogconnect.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.udyogconnect.data.local.CategoryEntity
import com.example.udyogconnect.data.local.RfqEntity
import com.example.udyogconnect.data.local.SupplierProfileEntity
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.data.model.UserRole
import com.example.udyogconnect.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentRole: UserRole,
    language: Language,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    categories: List<CategoryEntity>,
    featuredSuppliers: List<SupplierProfileEntity>,
    recentRfqs: List<RfqEntity>,
    onSelectSupplier: (String) -> Unit,
    onSelectRfq: (String) -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigatePostRfq: () -> Unit,
    onNavigateRfqs: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceGray)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Hero Section with Generated Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_industrial_banner_1786505732751),
                contentDescription = "Industrial Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IndustrialNavy.copy(alpha = 0.75f))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = SafetyOrange
                ) {
                    Text(
                        text = if (language == Language.HINDI) "भारत का B2B विनिर्माण पोर्टल" else "India's B2B MSME Manufacturing Network",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (language == Language.HINDI) "वर्कशॉप्स को मिले सीधे बड़ी कंपनियों के ऑर्डर्स" else "Connect Small Workshops directly to Big Industry OEMs",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (language == Language.HINDI) "राजकोट, पुणे, फरीदाबाद, कोयंबटूर एवं अन्य प्रमुख औद्योगिक क्षेत्र" else "Verified MSMEs across Rajkot, Pune, Faridabad, Coimbatore, NCR & Ludhiana",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.LightGray
                    )
                )
            }
        }

        // Search Bar Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-20).dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        onSearchChange(it)
                        onNavigateSearch()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = if (language == Language.HINDI) "मशीन, प्रक्रिया (CNC, VMC, Laser) या शहर खोजें..." else "Search machines, processes (VMC, Laser, Gear) or city...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = SafetyOrange)
                    },
                    trailingIcon = {
                        IconButton(onClick = onNavigateSearch) {
                            Icon(imageVector = Icons.Default.Tune, contentDescription = "Filter", tint = IndustrialNavy)
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

                // Action Callouts based on Role
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onNavigatePostRfq,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SafetyOrange),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == Language.HINDI) "+ नया RFQ भेजें" else "+ Post New RFQ",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    OutlinedButton(
                        onClick = onNavigateSearch,
                        modifier = Modifier.weight(1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialNavy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Factory, contentDescription = null, tint = IndustrialNavy, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == Language.HINDI) "निर्माता खोजें" else "Find Suppliers",
                            style = MaterialTheme.typography.labelMedium.copy(color = IndustrialNavy, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // High Density Summary Stats Grid
        HighDensityStatsGrid(
            activeRfqsCount = recentRfqs.size.coerceAtLeast(12),
            pipelineValue = "₹4.2L",
            matchScore = "98%",
            language = language
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.padding(horizontal = 0.dp)) {
            // High Priority Matches Section
            SectionHeader(
                titleEn = "High Priority Matches",
                titleHi = "उच्च प्राथमिकता मैचेस",
                actionLabel = "View All",
                onActionClick = onNavigateRfqs,
                language = language
            )

            recentRfqs.take(3).forEachIndexed { index, rfq ->
                HighDensityRfqCard(
                    rfq = rfq,
                    language = language,
                    matchScore = if (index == 0) 98 else if (index == 1) 92 else 85,
                    onSelect = { onSelectRfq(rfq.id) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Categories Grid
            SectionHeader(
                titleEn = "Manufacturing Capabilities & Processes",
                titleHi = "विनिर्माण क्षमताएं एवं प्रक्रियाएं",
                actionLabel = "View All",
                onActionClick = onNavigateSearch,
                language = language
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(categories) { cat ->
                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .clickable { onNavigateSearch() },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(IndustrialNavy.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PrecisionManufacturing,
                                    contentDescription = null,
                                    tint = IndustrialNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (language == Language.HINDI) cat.nameHi else cat.nameEn,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IndustrialNavy
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Featured MSME Workshops
            SectionHeader(
                titleEn = "Verified MSME Workshops & Machine Shops",
                titleHi = "सत्यापित एमएसएमई वर्कशॉप एवं मशीन शॉप्स",
                actionLabel = "Directory",
                onActionClick = onNavigateSearch,
                language = language
            )

            featuredSuppliers.take(3).forEach { supplier ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
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
                                Spacer(modifier = Modifier.height(2.dp))
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

                        // Machine & Capability Tags
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val machines = supplier.machinesJson.replace("[", "").replace("]", "").replace("\"", "").split(",")
                            machines.take(2).forEach { m ->
                                MachineTag(tag = m.trim())
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = CardBorder)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RatingBar(rating = supplier.rating, reviewCount = supplier.reviewCount)
                            Text(
                                text = "Tolerance: ${supplier.tolerancesJson}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = IndustrialBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Recent RFQs
            SectionHeader(
                titleEn = "Live OEM RFQs & Requirements",
                titleHi = "लाइव खरीदार आवश्यकताएं (RFQs)",
                actionLabel = "View All RFQs",
                onActionClick = onNavigateRfqs,
                language = language
            )

            recentRfqs.take(2).forEach { rfq ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onSelectRfq(rfq.id) },
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
                            Text(
                                text = rfq.partName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IndustrialNavy
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                color = SafetyOrangeBg,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Qty: ${rfq.quantity} pcs",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SafetyOrange,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${rfq.buyerCompany} • ${rfq.locationPreference}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Material: ${rfq.material}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                            )
                            Text(
                                text = "Target: ₹${rfq.targetUnitPrice}/pc",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = VerifiedGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
