package com.example.udyogconnect.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.udyogconnect.data.local.*
import com.example.udyogconnect.data.model.*
import com.example.udyogconnect.data.repository.UdyogRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UdyogViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = UdyogRepository(db.appDao())

    // Active User State
    private val _currentUserId = MutableStateFlow("usr_buyer_1")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.BUYER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow(Language.ENGLISH)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    // UI Navigation & Selection State
    private val _selectedRfqId = MutableStateFlow<String?>("rfq_101")
    val selectedRfqId: StateFlow<String?> = _selectedRfqId.asStateFlow()

    private val _selectedSupplierId = MutableStateFlow<String?>("usr_sup_1")
    val selectedSupplierId: StateFlow<String?> = _selectedSupplierId.asStateFlow()

    private val _selectedOrderId = MutableStateFlow<String?>("ord_demo")
    val selectedOrderId: StateFlow<String?> = _selectedOrderId.asStateFlow()

    // Search & Filter State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterCity = MutableStateFlow("All Cities")
    val filterCity: StateFlow<String> = _filterCity.asStateFlow()

    private val _filterProcess = MutableStateFlow("All Processes")
    val filterProcess: StateFlow<String> = _filterProcess.asStateFlow()

    private val _filterMaterial = MutableStateFlow("All Materials")
    val filterMaterial: StateFlow<String> = _filterMaterial.asStateFlow()

    private val _filterVerification = MutableStateFlow("All Status")
    val filterVerification: StateFlow<String> = _filterVerification.asStateFlow()

    // Data Flows
    val currentUser: StateFlow<UserEntity?> = currentUserId
        .flatMapLatest { id -> repository.getUser(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allSuppliers: StateFlow<List<SupplierProfileEntity>> = repository.allSuppliers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredSuppliers: StateFlow<List<SupplierProfileEntity>> = combine(
        allSuppliers, searchQuery, filterCity, filterProcess
    ) { suppliers, query, city, process ->
        suppliers.filter { supplier ->
            val matchesQuery = query.isEmpty() ||
                    supplier.companyName.contains(query, ignoreCase = true) ||
                    supplier.city.contains(query, ignoreCase = true) ||
                    supplier.processesJson.contains(query, ignoreCase = true) ||
                    supplier.machinesJson.contains(query, ignoreCase = true)

            val matchesCity = city == "All Cities" || supplier.city.equals(city, ignoreCase = true)
            val matchesProcess = process == "All Processes" || supplier.processesJson.contains(process, ignoreCase = true)

            matchesQuery && matchesCity && matchesProcess
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRfqs: StateFlow<List<RfqEntity>> = repository.allRfqs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedRfq: StateFlow<RfqEntity?> = selectedRfqId
        .flatMapLatest { id -> id?.let { repository.getRfq(it) } ?: flowOf(null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedSupplierProfile: StateFlow<SupplierProfileEntity?> = selectedSupplierId
        .flatMapLatest { id -> id?.let { repository.getSupplier(it) } ?: flowOf(null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val quotesForSelectedRfq: StateFlow<List<QuoteEntity>> = selectedRfqId
        .flatMapLatest { id -> id?.let { repository.getQuotesForRfq(it) } ?: flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedOrder: StateFlow<OrderEntity?> = selectedOrderId
        .flatMapLatest { id -> id?.let { repository.getOrder(it) } ?: flowOf(null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeChatMessages: StateFlow<List<ChatMessageEntity>> = selectedRfqId
        .flatMapLatest { id -> id?.let { repository.getChatMessages(it) } ?: flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reviewsForSelectedSupplier: StateFlow<List<ReviewEntity>> = selectedSupplierId
        .flatMapLatest { id -> id?.let { repository.getReviews(it) } ?: flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
        }
    }

    // Translation Helper
    fun tr(en: String, hi: String): String {
        return if (_currentLanguage.value == Language.HINDI) hi else en
    }

    // Role & User Switching
    fun switchUserRole(role: UserRole) {
        _currentRole.value = role
        when (role) {
            UserRole.BUYER -> _currentUserId.value = "usr_buyer_1"
            UserRole.SUPPLIER -> _currentUserId.value = "usr_sup_1"
            UserRole.ADMIN -> _currentUserId.value = "usr_admin_1"
        }
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == Language.ENGLISH) Language.HINDI else Language.ENGLISH
    }

    // Filtering Actions
    fun updateSearchQuery(query: String) { _searchQuery.value = query }
    fun updateFilterCity(city: String) { _filterCity.value = city }
    fun updateFilterProcess(process: String) { _filterProcess.value = process }
    fun updateFilterMaterial(material: String) { _filterMaterial.value = material }
    fun updateFilterVerification(verification: String) { _filterVerification.value = verification }

    fun selectRfq(rfqId: String) { _selectedRfqId.value = rfqId }
    fun selectSupplier(supplierId: String) { _selectedSupplierId.value = supplierId }
    fun selectOrder(orderId: String) { _selectedOrderId.value = orderId }

    // Buyer Action: Post New RFQ
    fun postRfq(
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
    ) {
        viewModelScope.launch {
            val rfqId = "rfq_" + System.currentTimeMillis()
            val newRfq = RfqEntity(
                id = rfqId,
                buyerId = _currentUserId.value,
                buyerName = "Vikram Malhotra",
                buyerCompany = "IndoTech Heavy Equipment Ltd",
                partName = partName,
                category = category,
                material = material,
                processRequired = processRequired,
                dimensions = dimensions,
                tolerance = tolerance,
                quantity = quantity,
                targetUnitPrice = targetUnitPrice,
                finishCoating = finishCoating,
                deliveryDate = deliveryDate,
                drawingFileName = if (drawingFileName.isNotBlank()) drawingFileName else "Technical_Part_Spec.pdf",
                notes = notes,
                status = RfqStatus.OPEN.name,
                locationPreference = locationPref
            )
            repository.createRfq(newRfq)
            _selectedRfqId.value = rfqId
        }
    }

    // Supplier Action: Submit Detailed Quotation
    fun submitQuote(
        rfqId: String,
        unitPrice: Double,
        toolingCost: Double,
        moq: Int,
        gstPercent: Double,
        freightCost: Double,
        leadTimeDays: Int,
        paymentTerms: String,
        notes: String
    ) {
        viewModelScope.launch {
            val supplier = selectedSupplierProfile.value ?: allSuppliers.value.firstOrNull { it.supplierId == _currentUserId.value }
            val rfq = repository.getRfq(rfqId).firstOrNull() ?: return@launch

            val supplierId = supplier?.supplierId ?: _currentUserId.value
            val supplierName = supplier?.companyName ?: "Supplier Workshop"

            val matchScore = if (supplier != null) repository.computeMatchScore(supplier, rfq) else 85
            val totalPrice = (unitPrice * rfq.quantity) + toolingCost + freightCost
            val totalWithGst = totalPrice * (1 + (gstPercent / 100))

            val quoteId = "quote_" + System.currentTimeMillis()
            val newQuote = QuoteEntity(
                id = quoteId,
                rfqId = rfqId,
                supplierId = supplierId,
                supplierName = "Rajesh Sharma",
                supplierCompany = supplierName,
                supplierLocation = supplier?.city ?: "Rajkot, Gujarat",
                verificationStatus = supplier?.verificationStatus ?: VerificationStatus.VERIFIED_GST_MSME.name,
                unitPrice = unitPrice,
                toolingCost = toolingCost,
                moq = moq,
                gstPercentage = gstPercent,
                freightCost = freightCost,
                totalPrice = totalWithGst,
                leadTimeDays = leadTimeDays,
                paymentTerms = paymentTerms,
                matchScore = matchScore,
                notes = notes,
                status = QuoteStatus.SUBMITTED.name
            )
            repository.submitQuote(newQuote)
            repository.updateRfqStatus(rfqId, RfqStatus.IN_REVIEW.name)
        }
    }

    // Buyer Action: Accept Quote -> Create Order
    fun acceptQuote(quote: QuoteEntity, rfq: RfqEntity) {
        viewModelScope.launch {
            repository.updateQuoteStatus(quote.id, QuoteStatus.ACCEPTED.name)
            repository.updateRfqStatus(rfq.id, RfqStatus.AWARDED.name)

            val orderId = "ord_" + System.currentTimeMillis()
            val poNum = "PO-IND-" + (1000..9999).random()

            val newOrder = OrderEntity(
                id = orderId,
                rfqId = rfq.id,
                quoteId = quote.id,
                buyerId = rfq.buyerId,
                buyerCompany = rfq.buyerCompany,
                supplierId = quote.supplierId,
                supplierCompany = quote.supplierCompany,
                partName = rfq.partName,
                quantity = rfq.quantity,
                unitPrice = quote.unitPrice,
                totalAmount = quote.totalPrice,
                status = OrderStatus.QUOTE_ACCEPTED.name,
                poNumber = poNum,
                trackingNotes = "Quote accepted. Purchase Order $poNum generated automatically.",
                estimatedDeliveryDate = rfq.deliveryDate
            )
            repository.createOrder(newOrder)
            _selectedOrderId.value = orderId
        }
    }

    // Order Lifecycle Advancement
    fun advanceOrderStatus(order: OrderEntity, nextStatus: OrderStatus, notes: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(order.id, nextStatus.name, notes)
        }
    }

    // Chat Messaging
    fun sendChatMessage(rfqId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val user = currentUser.value
            val msg = ChatMessageEntity(
                id = "msg_" + System.currentTimeMillis(),
                rfqId = rfqId,
                senderId = _currentUserId.value,
                senderName = user?.name ?: "User",
                receiverId = if (_currentRole.value == UserRole.BUYER) "usr_sup_1" else "usr_buyer_1",
                message = text
            )
            repository.sendMessage(msg)
        }
    }

    // Admin Verification Approval
    fun adminUpdateVerification(supplierId: String, newStatus: VerificationStatus) {
        viewModelScope.launch {
            repository.updateSupplierVerification(supplierId, newStatus.name)
            repository.updateUserVerification(supplierId, newStatus.name)
        }
    }

    // Submit Review
    fun submitReview(supplierId: String, orderId: String, rating: Float, comment: String) {
        viewModelScope.launch {
            val review = ReviewEntity(
                id = "rev_" + System.currentTimeMillis(),
                orderId = orderId,
                supplierId = supplierId,
                buyerName = currentUser.value?.name ?: "Verified Buyer",
                buyerCompany = currentUser.value?.companyName ?: "OEM Industry",
                rating = rating,
                comment = comment
            )
            repository.submitReview(review)
        }
    }

    // Match score helper for UI
    fun getMatchScoreForSupplier(supplier: SupplierProfileEntity, rfq: RfqEntity): Int {
        return repository.computeMatchScore(supplier, rfq)
    }
}
