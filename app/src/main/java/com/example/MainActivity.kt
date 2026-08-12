package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.MyApplicationTheme
import com.example.udyogconnect.data.local.OrderEntity
import com.example.udyogconnect.data.local.QuoteEntity
import com.example.udyogconnect.data.local.RfqEntity
import com.example.udyogconnect.data.model.Language
import com.example.udyogconnect.data.model.UserRole
import com.example.udyogconnect.ui.components.IndustrialTopBar
import com.example.udyogconnect.ui.screens.*
import com.example.ui.theme.*
import com.example.udyogconnect.ui.viewmodel.UdyogViewModel

enum class NavTab {
    HOME, SUPPLIERS, RFQS, DASHBOARD, PROFILE
}

enum class SubScreen {
    NONE, SUPPLIER_DETAIL, RFQ_DETAIL, RFQ_CREATE, QUOTE_CREATE, ORDER_LIFECYCLE, CHAT, ADMIN_CONSOLE
}

class MainActivity : ComponentActivity() {

    private val viewModel: UdyogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                UdyogConnectApp(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UdyogConnectApp(viewModel: UdyogViewModel) {
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCity by viewModel.filterCity.collectAsStateWithLifecycle()
    val selectedProcess by viewModel.filterProcess.collectAsStateWithLifecycle()

    val filteredSuppliers by viewModel.filteredSuppliers.collectAsStateWithLifecycle()
    val allSuppliers by viewModel.allSuppliers.collectAsStateWithLifecycle()
    val allRfqs by viewModel.allRfqs.collectAsStateWithLifecycle()
    val selectedRfq by viewModel.selectedRfq.collectAsStateWithLifecycle()
    val selectedSupplierProfile by viewModel.selectedSupplierProfile.collectAsStateWithLifecycle()
    val quotesForSelectedRfq by viewModel.quotesForSelectedRfq.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val selectedOrder by viewModel.selectedOrder.collectAsStateWithLifecycle()
    val activeChatMessages by viewModel.activeChatMessages.collectAsStateWithLifecycle()
    val reviewsForSelectedSupplier by viewModel.reviewsForSelectedSupplier.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(NavTab.HOME) }
    var activeSubScreen by remember { mutableStateOf(SubScreen.NONE) }

    Scaffold(
        topBar = {
            if (activeSubScreen == SubScreen.NONE) {
                IndustrialTopBar(
                    currentRole = currentRole,
                    currentLanguage = currentLanguage,
                    onRoleSelect = { role ->
                        viewModel.switchUserRole(role)
                        if (role == UserRole.ADMIN) {
                            activeSubScreen = SubScreen.ADMIN_CONSOLE
                        }
                    },
                    onLanguageToggle = { viewModel.toggleLanguage() },
                    onOpenProfile = { activeTab = NavTab.PROFILE }
                )
            }
        },
        bottomBar = {
            if (activeSubScreen == SubScreen.NONE) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    NavigationBarItem(
                        selected = activeTab == NavTab.HOME,
                        onClick = { activeTab = NavTab.HOME },
                        icon = { Icon(imageVector = if (activeTab == NavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "Home") },
                        label = { Text(text = if (currentLanguage == Language.HINDI) "होम" else "Home", fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = SafetyOrange, indicatorColor = SafetyOrangeBg)
                    )
                    NavigationBarItem(
                        selected = activeTab == NavTab.SUPPLIERS,
                        onClick = { activeTab = NavTab.SUPPLIERS },
                        icon = { Icon(imageVector = if (activeTab == NavTab.SUPPLIERS) Icons.Filled.PrecisionManufacturing else Icons.Outlined.PrecisionManufacturing, contentDescription = "Suppliers") },
                        label = { Text(text = if (currentLanguage == Language.HINDI) "निर्माता" else "Suppliers", fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = SafetyOrange, indicatorColor = SafetyOrangeBg)
                    )
                    NavigationBarItem(
                        selected = activeTab == NavTab.RFQS,
                        onClick = { activeTab = NavTab.RFQS },
                        icon = { Icon(imageVector = if (activeTab == NavTab.RFQS) Icons.Filled.Assignment else Icons.Outlined.Assignment, contentDescription = "RFQs") },
                        label = { Text(text = if (currentLanguage == Language.HINDI) "RFQs" else "RFQs", fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = SafetyOrange, indicatorColor = SafetyOrangeBg)
                    )
                    NavigationBarItem(
                        selected = activeTab == NavTab.DASHBOARD,
                        onClick = { activeTab = NavTab.DASHBOARD },
                        icon = { Icon(imageVector = if (activeTab == NavTab.DASHBOARD) Icons.Filled.Dashboard else Icons.Outlined.Dashboard, contentDescription = "Dashboard") },
                        label = { Text(text = if (currentLanguage == Language.HINDI) "डैशबोर्ड" else "Dashboard", fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = SafetyOrange, indicatorColor = SafetyOrangeBg)
                    )
                    NavigationBarItem(
                        selected = activeTab == NavTab.PROFILE,
                        onClick = { activeTab = NavTab.PROFILE },
                        icon = { Icon(imageVector = if (activeTab == NavTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person, contentDescription = "Profile") },
                        label = { Text(text = if (currentLanguage == Language.HINDI) "प्रोफाइल" else "Profile", fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = SafetyOrange, indicatorColor = SafetyOrangeBg)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (activeSubScreen) {
                SubScreen.SUPPLIER_DETAIL -> {
                    SupplierDetailScreen(
                        supplier = selectedSupplierProfile,
                        reviews = reviewsForSelectedSupplier,
                        language = currentLanguage,
                        onBack = { activeSubScreen = SubScreen.NONE },
                        onRequestQuote = {
                            activeSubScreen = SubScreen.RFQ_CREATE
                        },
                        onOpenChat = {
                            activeSubScreen = SubScreen.CHAT
                        }
                    )
                }
                SubScreen.RFQ_DETAIL -> {
                    RfqDetailScreen(
                        rfq = selectedRfq,
                        quotes = quotesForSelectedRfq,
                        currentRole = currentRole,
                        language = currentLanguage,
                        onBack = { activeSubScreen = SubScreen.NONE },
                        onSubmitQuote = { rfqId ->
                            viewModel.selectRfq(rfqId)
                            activeSubScreen = SubScreen.QUOTE_CREATE
                        },
                        onAcceptQuote = { quote, rfq ->
                            viewModel.acceptQuote(quote, rfq)
                            activeSubScreen = SubScreen.ORDER_LIFECYCLE
                        },
                        onOpenChat = { rfqId ->
                            viewModel.selectRfq(rfqId)
                            activeSubScreen = SubScreen.CHAT
                        }
                    )
                }
                SubScreen.RFQ_CREATE -> {
                    RfqCreateScreen(
                        language = currentLanguage,
                        onBack = { activeSubScreen = SubScreen.NONE },
                        onPostRfq = { partName, category, material, processRequired, dimensions, tolerance, quantity, targetUnitPrice, finishCoating, deliveryDate, drawingFileName, notes, locationPref ->
                            viewModel.postRfq(
                                partName, category, material, processRequired, dimensions, tolerance, quantity, targetUnitPrice, finishCoating, deliveryDate, drawingFileName, notes, locationPref
                            )
                            activeSubScreen = SubScreen.RFQ_DETAIL
                        }
                    )
                }
                SubScreen.QUOTE_CREATE -> {
                    QuoteCreateScreen(
                        rfq = selectedRfq,
                        language = currentLanguage,
                        onBack = { activeSubScreen = SubScreen.RFQ_DETAIL },
                        onSubmitQuote = { rfqId, unitPrice, toolingCost, moq, gstPercent, freightCost, leadTimeDays, paymentTerms, notes ->
                            viewModel.submitQuote(
                                rfqId, unitPrice, toolingCost, moq, gstPercent, freightCost, leadTimeDays, paymentTerms, notes
                            )
                            activeSubScreen = SubScreen.RFQ_DETAIL
                        }
                    )
                }
                SubScreen.ORDER_LIFECYCLE -> {
                    OrderLifecycleScreen(
                        order = selectedOrder,
                        currentRole = currentRole,
                        language = currentLanguage,
                        onBack = { activeSubScreen = SubScreen.NONE },
                        onAdvanceStatus = { order, nextStatus, notes ->
                            viewModel.advanceOrderStatus(order, nextStatus, notes)
                        },
                        onSubmitReview = { supplierId, orderId, rating, comment ->
                            viewModel.submitReview(supplierId, orderId, rating, comment)
                        },
                        onOpenChat = { rfqId ->
                            viewModel.selectRfq(rfqId)
                            activeSubScreen = SubScreen.CHAT
                        }
                    )
                }
                SubScreen.CHAT -> {
                    ChatScreen(
                        rfqId = selectedRfq?.id ?: "rfq_101",
                        currentUserId = viewModel.currentUserId.value,
                        messages = activeChatMessages,
                        onBack = { activeSubScreen = SubScreen.NONE },
                        onSendMessage = { rfqId, text ->
                            viewModel.sendChatMessage(rfqId, text)
                        }
                    )
                }
                SubScreen.ADMIN_CONSOLE -> {
                    AdminScreen(
                        suppliers = allSuppliers,
                        language = currentLanguage,
                        onApproveVerification = { supplierId, status ->
                            viewModel.adminUpdateVerification(supplierId, status)
                        }
                    )
                }
                SubScreen.NONE -> {
                    when (activeTab) {
                        NavTab.HOME -> {
                            HomeScreen(
                                currentRole = currentRole,
                                language = currentLanguage,
                                searchQuery = searchQuery,
                                onSearchChange = { viewModel.updateSearchQuery(it) },
                                categories = categories,
                                featuredSuppliers = allSuppliers,
                                recentRfqs = allRfqs,
                                onSelectSupplier = { id ->
                                    viewModel.selectSupplier(id)
                                    activeSubScreen = SubScreen.SUPPLIER_DETAIL
                                },
                                onSelectRfq = { id ->
                                    viewModel.selectRfq(id)
                                    activeSubScreen = SubScreen.RFQ_DETAIL
                                },
                                onNavigateSearch = { activeTab = NavTab.SUPPLIERS },
                                onNavigatePostRfq = { activeSubScreen = SubScreen.RFQ_CREATE },
                                onNavigateRfqs = { activeTab = NavTab.RFQS }
                            )
                        }
                        NavTab.SUPPLIERS -> {
                            SupplierSearchScreen(
                                suppliers = filteredSuppliers,
                                searchQuery = searchQuery,
                                onSearchChange = { viewModel.updateSearchQuery(it) },
                                selectedCity = selectedCity,
                                onCitySelect = { viewModel.updateFilterCity(it) },
                                selectedProcess = selectedProcess,
                                onProcessSelect = { viewModel.updateFilterProcess(it) },
                                language = currentLanguage,
                                onSelectSupplier = { id ->
                                    viewModel.selectSupplier(id)
                                    activeSubScreen = SubScreen.SUPPLIER_DETAIL
                                },
                                onPostRfqForSupplier = { id ->
                                    viewModel.selectSupplier(id)
                                    activeSubScreen = SubScreen.RFQ_CREATE
                                }
                            )
                        }
                        NavTab.RFQS -> {
                            RfqListScreen(
                                rfqs = allRfqs,
                                currentRole = currentRole,
                                language = currentLanguage,
                                onSelectRfq = { id ->
                                    viewModel.selectRfq(id)
                                    activeSubScreen = SubScreen.RFQ_DETAIL
                                },
                                onPostRfq = { activeSubScreen = SubScreen.RFQ_CREATE },
                                onSubmitQuote = { rfqId ->
                                    viewModel.selectRfq(rfqId)
                                    activeSubScreen = SubScreen.QUOTE_CREATE
                                }
                            )
                        }
                        NavTab.DASHBOARD -> {
                            DashboardScreen(
                                currentRole = currentRole,
                                language = currentLanguage,
                                rfqs = allRfqs,
                                orders = allOrders,
                                onSelectRfq = { id ->
                                    viewModel.selectRfq(id)
                                    activeSubScreen = SubScreen.RFQ_DETAIL
                                },
                                onSelectOrder = { id ->
                                    viewModel.selectOrder(id)
                                    activeSubScreen = SubScreen.ORDER_LIFECYCLE
                                },
                                onNavigatePostRfq = { activeSubScreen = SubScreen.RFQ_CREATE }
                            )
                        }
                        NavTab.PROFILE -> {
                            ProfileScreen(
                                user = currentUser,
                                currentRole = currentRole,
                                language = currentLanguage,
                                onRoleSelect = { role -> viewModel.switchUserRole(role) },
                                onLanguageToggle = { viewModel.toggleLanguage() }
                            )
                        }
                    }
                }
            }
        }
    }
}
