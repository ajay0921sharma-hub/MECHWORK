package com.example.udyogconnect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.data.model.UserRole
import com.example.udyogconnect.data.model.VerificationStatus
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndustrialTopBar(
    currentRole: UserRole,
    currentLanguage: Language,
    onRoleSelect: (UserRole) -> Unit,
    onLanguageToggle: () -> Unit,
    onOpenProfile: () -> Unit,
    searchQuery: String = "",
    onSearchQueryChange: ((String) -> Unit)? = null
) {
    Surface(
        color = IndustrialNavy,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)
        ) {
            // Upper Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MECHWORK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SafetyOrange,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        text = if (currentRole == UserRole.SUPPLIER) "Precision CNC, Pune" else if (currentRole == UserRole.BUYER) "Anand Automotives, Faridabad" else "MECHWORK Admin Console",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Language Switcher
                    Surface(
                        onClick = onLanguageToggle,
                        shape = RoundedCornerShape(20.dp),
                        color = IndustrialNavyLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialNavyBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Translate,
                                contentDescription = "Language",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (currentLanguage == Language.ENGLISH) "हिंदी" else "EN",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    // Notification Icon with Orange Badge Dot
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(IndustrialNavyLight)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SafetyOrange)
                        )
                    }

                    // Profile / Persona Circle Avatar
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SafetyOrange)
                            .clickable { onOpenProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentRole == UserRole.SUPPLIER) "PC" else if (currentRole == UserRole.BUYER) "AA" else "AD",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Header Search Bar (Matching High Density Theme)
            if (onSearchQueryChange != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = IndustrialNavyLight.copy(alpha = 0.7f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialNavyBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Slate400,
                            modifier = Modifier.size(20.dp)
                        )
                        androidx.compose.foundation.text.BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontSize = 13.sp
                            ),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = if (currentLanguage == Language.HINDI) "खोजें RFQs, खरीदार, सामग्री..." else "Search RFQs, Buyers, Materials...",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Slate400,
                                            fontSize = 13.sp
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        )
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = "Filter",
                            tint = Slate400,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Role Persona Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UserRole.values().forEach { role ->
                    val isSelected = role == currentRole
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onRoleSelect(role) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) SafetyOrange else IndustrialNavyLight,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, IndustrialNavyBorder)
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (currentLanguage == Language.HINDI) role.labelHi else role.labelEn.split("/").first().trim(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerificationBadge(statusString: String) {
    val status = try { VerificationStatus.valueOf(statusString) } catch (e: Exception) { VerificationStatus.UNVERIFIED }
    
    val bg = when (status) {
        VerificationStatus.VERIFIED_GST_MSME -> VerifiedGreenBg
        VerificationStatus.GST_ONLY -> Color(0xFFEFF6FF)
        VerificationStatus.PENDING_VERIFICATION -> PendingAmberBg
        VerificationStatus.UNVERIFIED -> Color(0xFFF1F5F9)
    }

    val textColor = when (status) {
        VerificationStatus.VERIFIED_GST_MSME -> VerifiedGreen
        VerificationStatus.GST_ONLY -> IndustrialBlueLight
        VerificationStatus.PENDING_VERIFICATION -> PendingAmber
        VerificationStatus.UNVERIFIED -> TextMuted
    }

    val icon = when (status) {
        VerificationStatus.VERIFIED_GST_MSME -> Icons.Filled.Verified
        VerificationStatus.GST_ONLY -> Icons.Filled.CheckCircle
        VerificationStatus.PENDING_VERIFICATION -> Icons.Outlined.HourglassTop
        VerificationStatus.UNVERIFIED -> Icons.Outlined.HelpOutline
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = status.labelEn,
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        )
    }
}

@Composable
fun MatchScoreChip(score: Int) {
    val bg = if (score >= 90) VerifiedGreenBg else if (score >= 75) PendingAmberBg else Color(0xFFF1F5F9)
    val color = if (score >= 90) VerifiedGreen else if (score >= 75) PendingAmber else TextSecondary

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Psychology,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "$score% Match",
            style = MaterialTheme.typography.labelSmall.copy(
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun MachineTag(tag: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFF1F5F9),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun RatingBar(rating: Float, reviewCount: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color(0xFFF59E0B),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = String.format("%.1f", rating),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "($reviewCount)",
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextMuted
            )
        )
    }
}

@Composable
fun SectionHeader(
    titleEn: String,
    titleHi: String = "",
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    language: Language = Language.ENGLISH
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (language == Language.HINDI && titleHi.isNotEmpty()) titleHi else titleEn,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 16.sp
            )
        )
        if (actionLabel != null && onActionClick != null) {
            TextButton(
                onClick = onActionClick,
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
            ) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = SafetyOrangeDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = SafetyOrangeDark,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun HighDensityStatsGrid(
    activeRfqsCount: Int = 12,
    pipelineValue: String = "₹4.2L",
    matchScore: String = "98%",
    language: Language = Language.ENGLISH
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Card 1
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$activeRfqsCount",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = SafetyOrangeDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = if (language == Language.HINDI) "सक्रिय RFQs" else "ACTIVE RFQS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Slate500,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Card 2
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = pipelineValue,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = IndustrialBlueLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = if (language == Language.HINDI) "पाइपलाइन वैल्यू" else "PIPELINE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Slate500,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Card 3
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = matchScore,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = VerifiedGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = if (language == Language.HINDI) "मैच स्कोर" else "MATCH SCORE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Slate500,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HighDensityRfqCard(
    rfq: com.example.udyogconnect.data.local.RfqEntity,
    language: Language = Language.ENGLISH,
    matchScore: Int = 98,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        shadowElevation = 2.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Orange Left Accent Strip (border-l-4 border-l-orange-500)
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(SafetyOrange)
            )

            Column(modifier = Modifier.weight(1f)) {
                // Top Header Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Slate100
                            ) {
                                Text(
                                    text = "#${rfq.id.take(8).uppercase()}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Slate600,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SafetyOrangeBg
                            ) {
                                Text(
                                    text = if (matchScore >= 90) "High Match" else "Moderate Match",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SafetyOrangeDark,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = rfq.partName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Slate900,
                                fontSize = 15.sp
                            )
                        )
                        Text(
                            text = "Buyer: ${rfq.buyerCompany}, ${rfq.locationPreference}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Slate600,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "QUANTITY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Slate400,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        )
                        Text(
                            text = "${java.text.NumberFormat.getNumberInstance().format(rfq.quantity)} Pcs",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Slate900,
                                fontSize = 13.sp
                            )
                        )
                    }
                }

                // 2x2 Details Grid Panel
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Slate50)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = null,
                                    tint = Slate400,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = rfq.processRequired,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Slate700,
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Architecture,
                                    contentDescription = null,
                                    tint = Slate400,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${rfq.material} | ${rfq.tolerance}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Slate700,
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CalendarToday,
                                    contentDescription = null,
                                    tint = Slate400,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Target: ${rfq.deliveryDate}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Slate700,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Attachment,
                                    contentDescription = null,
                                    tint = SafetyOrangeDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (rfq.drawingFileName.isNotEmpty()) rfq.drawingFileName else "Technical_DWG.pdf",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SafetyOrangeDark,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // Dark Navy Full-Width Button
                Button(
                    onClick = onSelect,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndustrialNavy)
                ) {
                    Text(
                        text = if (language == Language.HINDI) "सबमिट कोटेशन / विवरण देखें" else "Submit Quotation / View Details",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

