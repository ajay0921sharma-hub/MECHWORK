package com.example.udyogconnect.data.repository

import com.example.udyogconnect.data.local.*
import com.example.udyogconnect.data.model.VerificationStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONArray

class UdyogRepository(private val dao: AppDao) {

    // Users & Roles
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()
    fun getUser(id: String): Flow<UserEntity?> = dao.getUserById(id)
    suspend fun saveUser(user: UserEntity) = dao.insertUser(user)
    suspend fun updateUserVerification(userId: String, status: String) = dao.updateUserVerification(userId, status)

    // Suppliers
    val allSuppliers: Flow<List<SupplierProfileEntity>> = dao.getAllSupplierProfiles()
    fun getSupplier(supplierId: String): Flow<SupplierProfileEntity?> = dao.getSupplierProfileById(supplierId)
    suspend fun saveSupplierProfile(profile: SupplierProfileEntity) = dao.insertSupplierProfile(profile)
    suspend fun updateSupplierVerification(supplierId: String, status: String) = dao.updateSupplierVerification(supplierId, status)

    // RFQs
    val allRfqs: Flow<List<RfqEntity>> = dao.getAllRfqs()
    fun getRfqsForBuyer(buyerId: String): Flow<List<RfqEntity>> = dao.getRfqsByBuyer(buyerId)
    fun getRfq(rfqId: String): Flow<RfqEntity?> = dao.getRfqById(rfqId)
    suspend fun createRfq(rfq: RfqEntity) = dao.insertRfq(rfq)
    suspend fun updateRfqStatus(rfqId: String, status: String) = dao.updateRfqStatus(rfqId, status)

    // Quotes
    fun getQuotesForRfq(rfqId: String): Flow<List<QuoteEntity>> = dao.getQuotesForRfq(rfqId)
    fun getQuotesBySupplier(supplierId: String): Flow<List<QuoteEntity>> = dao.getQuotesBySupplier(supplierId)
    fun getQuote(quoteId: String): Flow<QuoteEntity?> = dao.getQuoteById(quoteId)
    suspend fun submitQuote(quote: QuoteEntity) = dao.insertQuote(quote)
    suspend fun updateQuoteStatus(quoteId: String, status: String) = dao.updateQuoteStatus(quoteId, status)

    // Orders
    val allOrders: Flow<List<OrderEntity>> = dao.getAllOrders()
    fun getOrdersForBuyer(buyerId: String): Flow<List<OrderEntity>> = dao.getOrdersForBuyer(buyerId)
    fun getOrdersForSupplier(supplierId: String): Flow<List<OrderEntity>> = dao.getOrdersForSupplier(supplierId)
    fun getOrder(orderId: String): Flow<OrderEntity?> = dao.getOrderById(orderId)
    suspend fun createOrder(order: OrderEntity) = dao.insertOrder(order)
    suspend fun updateOrderStatus(orderId: String, status: String, notes: String) = dao.updateOrderStatus(orderId, status, notes)

    // Chat Messages
    fun getChatMessages(rfqId: String): Flow<List<ChatMessageEntity>> = dao.getChatMessagesForRfq(rfqId)
    suspend fun sendMessage(msg: ChatMessageEntity) = dao.insertChatMessage(msg)

    // Reviews
    fun getReviews(supplierId: String): Flow<List<ReviewEntity>> = dao.getReviewsForSupplier(supplierId)
    suspend fun submitReview(review: ReviewEntity) = dao.insertReview(review)

    // Categories
    val allCategories: Flow<List<CategoryEntity>> = dao.getAllCategories()

    // Smart RFQ Matching Score Algorithm
    fun computeMatchScore(supplier: SupplierProfileEntity, rfq: RfqEntity): Int {
        var score = 40 // Base compatibility score

        val processes = parseJsonArray(supplier.processesJson)
        val materials = parseJsonArray(supplier.materialsJson)
        val machines = parseJsonArray(supplier.machinesJson)

        // Process Match (+25)
        if (processes.any { it.contains(rfq.processRequired, ignoreCase = true) || rfq.processRequired.contains(it, ignoreCase = true) }) {
            score += 25
        } else if (processes.isNotEmpty()) {
            score += 10
        }

        // Material Match (+15)
        if (materials.any { it.contains(rfq.material, ignoreCase = true) || rfq.material.contains(it, ignoreCase = true) }) {
            score += 15
        } else if (materials.isNotEmpty()) {
            score += 5
        }

        // Verification Bonus (+10)
        if (supplier.verificationStatus == VerificationStatus.VERIFIED_GST_MSME.name) {
            score += 10
        } else if (supplier.verificationStatus == VerificationStatus.GST_ONLY.name) {
            score += 5
        }

        // Location Proximity (+10)
        if (rfq.locationPreference.contains(supplier.city, ignoreCase = true) || rfq.locationPreference == "All India") {
            score += 10
        }

        return score.coerceAtMost(98)
    }

    private fun parseJsonArray(jsonStr: String): List<String> {
        return try {
            val jsonArray = JSONArray(jsonStr)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Seed Data Initialization
    suspend fun seedDatabaseIfEmpty() {
        val existingUsers = dao.getAllUsers().firstOrNull()
        if (!existingUsers.isNullOrEmpty()) return

        // Seed Default Users
        val demoUsers = listOf(
            UserEntity(
                id = "usr_sup_1",
                role = "SUPPLIER",
                name = "Rajesh Sharma",
                companyName = "Ambika Precision Engineering",
                email = "rajesh@ambikaprecision.in",
                phone = "+91 98250 12345",
                city = "Rajkot",
                state = "Gujarat",
                verificationStatus = VerificationStatus.VERIFIED_GST_MSME.name
            ),
            UserEntity(
                id = "usr_sup_2",
                role = "SUPPLIER",
                name = "Sardar Gurpreet Singh",
                companyName = "Shiv Shakti Fabrication & Laser",
                email = "gurpreet@shivshaktilaser.com",
                phone = "+91 98110 56789",
                city = "Faridabad",
                state = "Haryana",
                verificationStatus = VerificationStatus.VERIFIED_GST_MSME.name
            ),
            UserEntity(
                id = "usr_sup_3",
                role = "SUPPLIER",
                name = "Anand Kulkarni",
                companyName = "Apex Auto Components Pvt Ltd",
                email = "anand@apexauto.co.in",
                phone = "+91 98220 99887",
                city = "Pune",
                state = "Maharashtra",
                verificationStatus = VerificationStatus.GST_ONLY.name
            ),
            UserEntity(
                id = "usr_sup_4",
                role = "SUPPLIER",
                name = "S. Murugan",
                companyName = "Sri Lakshmi Toolings & CNC",
                email = "smurugan@srilakshmitools.com",
                phone = "+91 94430 44332",
                city = "Coimbatore",
                state = "Tamil Nadu",
                verificationStatus = VerificationStatus.VERIFIED_GST_MSME.name
            ),
            UserEntity(
                id = "usr_buyer_1",
                role = "BUYER",
                name = "Vikram Malhotra",
                companyName = "IndoTech Heavy Equipment Ltd (OEM)",
                email = "procurement@indotech.com",
                phone = "+91 98100 88776",
                city = "Gurugram",
                state = "Haryana",
                verificationStatus = VerificationStatus.VERIFIED_GST_MSME.name
            ),
            UserEntity(
                id = "usr_admin_1",
                role = "ADMIN",
                name = "UdyogConnect Admin Team",
                companyName = "UdyogConnect B2B Portal",
                email = "admin@udyogconnect.in",
                phone = "+91 1800 200 9000",
                city = "New Delhi",
                state = "Delhi",
                verificationStatus = VerificationStatus.VERIFIED_GST_MSME.name
            )
        )
        demoUsers.forEach { dao.insertUser(it) }

        // Seed Supplier Profiles
        val demoSuppliers = listOf(
            SupplierProfileEntity(
                supplierId = "usr_sup_1",
                companyName = "Ambika Precision Engineering",
                city = "Rajkot",
                state = "Gujarat",
                verificationStatus = VerificationStatus.VERIFIED_GST_MSME.name,
                machinesJson = "[\"VMC 4-Axis Machine (BFW)\", \"CNC Turning Center (Ace Micromatic)\", \"CMM Inspection Machine (Zeiss)\", \"Surface Grinder\"]",
                processesJson = "[\"CNC Machining\", \"VMC Precision Milling\", \"Precision Grinding\", \"Jig & Fixtures\"]",
                materialsJson = "[\"SS304 / SS316 Stainless Steel\", \"Aluminum 6061-T6\", \"Brass\", \"Alloy Steel EN19\"]",
                tolerancesJson = "±0.005 mm (5 Microns)",
                maxCapacityMonthly = "35,000 components/month",
                gstNumber = "24AABCA1234F1Z5",
                msmeUdyamNumber = "UDYAM-GJ-17-0012894",
                certificationsJson = "[\"ISO 9001:2015\", \"IATF 16949 Certified\"]",
                factoryPhotosJson = "[\"cnc_shopfloor\", \"vmc_setup\", \"inspection_lab\"]",
                rating = 4.9f,
                reviewCount = 38,
                pastClientsJson = "[\"Tata Motors Vendor Tier-2\", \"L&T Defense Equipment\", \"Bharat Forge Tier-2\"]",
                totalOrdersCompleted = 142,
                responseTimeHours = 2,
                description = "Specialized in ultra-precision VMC machining and complex turned components for Automotive, Defense & Hydraulics."
            ),
            SupplierProfileEntity(
                supplierId = "usr_sup_2",
                companyName = "Shiv Shakti Fabrication & Laser",
                city = "Faridabad",
                state = "Haryana",
                verificationStatus = VerificationStatus.VERIFIED_GST_MSME.name,
                machinesJson = "[\"Fiber Laser Cutting 3kW (Bystronic)\", \"CNC Press Brake 150T (Amada)\", \"MIG/TIG Welding Station\", \"Powder Coating Plant\"]",
                processesJson = "[\"Sheet Metal Fabrication\", \"Laser Cutting\", \"CNC Bending\", \"Powder Coating\", \"Structural Enclosures\"]",
                materialsJson = "[\"Mild Steel (CRCA/HRCA)\", \"SS304 Sheet\", \"Aluminum Sheet 5052\", \"Galvanized Iron\"]",
                tolerancesJson = "±0.05 mm",
                maxCapacityMonthly = "120,000 kg fabricated sheet metal/month",
                gstNumber = "06ABCFG5678H1Z2",
                msmeUdyamNumber = "UDYAM-HR-03-0045123",
                certificationsJson = "[\"ISO 9001:2015\"]",
                factoryPhotosJson = "[\"laser_cutting\", \"bending_line\"]",
                rating = 4.8f,
                reviewCount = 52,
                pastClientsJson = "[\"Schneider Electric Partner\", \"Havells India Vendor\", " +
                        "\"Escorts Kubota Supplier\"]",
                totalOrdersCompleted = 210,
                responseTimeHours = 1,
                description = "State-of-the-art sheet metal precision laser cutting, CNC bending, and powder coating unit catering to Electrical & Machinery OEMs."
            ),
            SupplierProfileEntity(
                supplierId = "usr_sup_3",
                companyName = "Apex Auto Components Pvt Ltd",
                city = "Pune",
                state = "Maharashtra",
                verificationStatus = VerificationStatus.GST_ONLY.name,
                machinesJson = "[\"CNC Hobbing Machine (Gleason)\", \"CNC Lathe (Doosan)\", \"Heat Treatment Furnace\", \"Gear Shaper\"]",
                processesJson = "[\"Gear Hobbing\", \"Transmission Shafts\", \"Heat Treatment\", \"CNC Turning\"]",
                materialsJson = "[\"20MnCr5 Gear Steel\", \"EN8 / EN24\", \"Alloy Steel\"]",
                tolerancesJson = "DIN Class 7 Gear Precision",
                maxCapacityMonthly = "25,000 gear assemblies/month",
                gstNumber = "27AAAC1234K1Z9",
                msmeUdyamNumber = "UDYAM-MH-26-0098765",
                certificationsJson = "[\"IATF 16949\"]",
                factoryPhotosJson = "[\"gear_hobbing\", \"heat_treat\"]",
                rating = 4.6f,
                reviewCount = 24,
                pastClientsJson = "[\"Mahindra Tractor Division\", \"Bajaj Auto Vendor\", \"Force Motors\"]",
                totalOrdersCompleted = 89,
                responseTimeHours = 4,
                description = "High precision transmission gears, helical shafts, and spline shafts manufacturing setup in Chakan Industrial Belt, Pune."
            ),
            SupplierProfileEntity(
                supplierId = "usr_sup_4",
                companyName = "Sri Lakshmi Toolings & CNC",
                city = "Coimbatore",
                state = "Tamil Nadu",
                verificationStatus = VerificationStatus.VERIFIED_GST_MSME.name,
                machinesJson = "[\"Wire Cut EDM (Sodic)\", \"CNC Jig Borer\", \"Precision Surface Grinder (Chevalier)\", \"EDM Die Sinker\"]",
                processesJson = "[\"Tooling & Dies\", \"Press Tools\", \"Plastic Injection Molds\", \"Wire EDM Machining\"]",
                materialsJson = "[\"OHNS Tool Steel\", \"D2 Die Steel\", \"Titanium Grade 5\", \"Hardened Tooling Steel\"]",
                tolerancesJson = "±0.002 mm (2 Microns)",
                maxCapacityMonthly = "40 Die sets & precision tooling molds/month",
                gstNumber = "33AABCS8899L1Z1",
                msmeUdyamNumber = "UDYAM-TN-03-0023411",
                certificationsJson = "[\"ISO 9001:2015\", \"MSME ZED Gold Certified\"]",
                factoryPhotosJson = "[\"toolroom\", \"wire_edm\"]",
                rating = 5.0f,
                reviewCount = 19,
                pastClientsJson = "[\"TVS Motors Tier-1\", \"Bosch India Subcontractor\"]",
                totalOrdersCompleted = 67,
                responseTimeHours = 2,
                description = "Coimbatore-based master toolroom specializing in high-precision die-casting molds, press tools, and sub-micron Wire EDM components."
            )
        )
        dao.insertSupplierProfiles(demoSuppliers)

        // Seed Categories
        val demoCategories = listOf(
            CategoryEntity("cat_cnc", "CNC Machining & Turning", "सीएनसी मशीनिंग एवं टर्निंग", "ic_cnc", "[\"VMC Milling\", \"CNC Lathe Turning\", \"Multi-Axis Machining\", \"Grinding\"]"),
            CategoryEntity("cat_sheet", "Sheet Metal & Laser Cutting", "शीट मेटल एवं लेजर कटिंग", "ic_laser", "[\"Fiber Laser Cutting\", \"CNC Bending\", \"MIG/TIG Welding\", \"Powder Coating\"]"),
            CategoryEntity("cat_tooling", "Tooling, Dies & Molds", "टूलिंग, डाइस एवं मोल्ड्स", "ic_mold", "[\"Press Tools\", \"Injection Molds\", \"Wire Cut EDM\", \"Jigs & Fixtures\"]"),
            CategoryEntity("cat_gears", "Gears, Shafts & Forging", "गियर्स, शाफ्ट्स एवं फोर्जिंग", "ic_gear", "[\"Gear Hobbing\", \"Hot Forging\", \"Spline Shafts\", \"Heat Treatment\"]"),
            CategoryEntity("cat_casting", "Casting & Foundry Work", "कास्टिंग एवं फाउंड्री", "ic_foundry", "[\"Investment Casting\", \"Aluminium Die Casting\", \"CI Foundry\", \"Sand Casting\"]"),
            CategoryEntity("cat_plastics", "Plastic Injection Molding", "प्लास्टिक मोल्डिंग", "ic_molding", "[\"ABS Molding\", \"Nylon Components\", \"Polycarbonate\", \"Blow Molding\"]")
        )
        dao.insertCategories(demoCategories)

        // Seed RFQs
        val demoRfqs = listOf(
            RfqEntity(
                id = "rfq_101",
                buyerId = "usr_buyer_1",
                buyerName = "Vikram Malhotra",
                buyerCompany = "IndoTech Heavy Equipment Ltd",
                partName = "VMC Machined Aluminum Motor Mount Bracket",
                category = "CNC Machining & Turning",
                material = "Aluminum 6061-T6",
                processRequired = "CNC Machining",
                dimensions = "180mm x 120mm x 45mm",
                tolerance = "±0.01 mm",
                quantity = 2500,
                targetUnitPrice = 480.0,
                finishCoating = "Black Anodized (Mil-A-8625)",
                deliveryDate = "15 Sep 2026",
                drawingFileName = "Motor_Mount_Bracket_Rev3.pdf",
                notes = "Requires 100% CMM inspection report with first batch. Material test certificate (MTC) required.",
                status = "OPEN",
                locationPreference = "Rajkot, Pune, Gurugram"
            ),
            RfqEntity(
                id = "rfq_102",
                buyerId = "usr_buyer_1",
                buyerName = "Vikram Malhotra",
                buyerCompany = "IndoTech Heavy Equipment Ltd",
                partName = "SS304 Electrical Control Panel Enclosure",
                category = "Sheet Metal & Laser Cutting",
                material = "SS304 Stainless Steel (2.0mm)",
                processRequired = "Sheet Metal Fabrication",
                dimensions = "600mm x 400mm x 250mm",
                tolerance = "±0.05 mm",
                quantity = 400,
                targetUnitPrice = 1850.0,
                finishCoating = "Matte Brushed Finish",
                deliveryDate = "30 Aug 2026",
                drawingFileName = "Control_Box_Assembly_SS304.pdf",
                notes = "IP65 weather-proof gaskets and lock cutouts required. Laser cutting & CNC press brake bending.",
                status = "OPEN",
                locationPreference = "Faridabad, Noida, Delhi NCR"
            ),
            RfqEntity(
                id = "rfq_103",
                buyerId = "usr_buyer_1",
                buyerName = "Vikram Malhotra",
                buyerCompany = "IndoTech Heavy Equipment Ltd",
                partName = "Precision Helical Transmission Gear Shaft",
                category = "Gears, Shafts & Forging",
                material = "20MnCr5 Alloy Steel",
                processRequired = "Gear Hobbing",
                dimensions = "Dia 65mm x Length 240mm",
                tolerance = "DIN Class 7",
                quantity = 1200,
                targetUnitPrice = 720.0,
                finishCoating = "Case Hardened (58-62 HRC)",
                deliveryDate = "20 Sep 2026",
                drawingFileName = "Gear_Shaft_Helical_Z32.pdf",
                notes = "Carburizing & tempering heat treatment required. Runout within 8 microns.",
                status = "IN_REVIEW",
                locationPreference = "Pune, Rajkot"
            )
        )
        dao.insertRfqs(demoRfqs)

        // Seed Sample Quotation
        val demoQuote = QuoteEntity(
            id = "q_201",
            rfqId = "rfq_101",
            supplierId = "usr_sup_1",
            supplierName = "Rajesh Sharma",
            supplierCompany = "Ambika Precision Engineering",
            supplierLocation = "Rajkot, Gujarat",
            verificationStatus = VerificationStatus.VERIFIED_GST_MSME.name,
            unitPrice = 465.0,
            toolingCost = 3500.0,
            moq = 500,
            gstPercentage = 18.0,
            freightCost = 8.0,
            totalPrice = 1215000.0, // Total for 2500 pcs + tooling + GST
            leadTimeDays = 14,
            paymentTerms = "30% Advance, 70% against Invoice & Dispatch",
            matchScore = 96,
            notes = "We have 2 dedicated 4-Axis VMCs ready. Full Zeiss CMM report and MTC included.",
            status = "SUBMITTED"
        )
        dao.insertQuote(demoQuote)

        // Seed Sample Chat
        val demoChats = listOf(
            ChatMessageEntity(
                id = "msg_1",
                rfqId = "rfq_101",
                senderId = "usr_buyer_1",
                senderName = "Vikram Malhotra",
                receiverId = "usr_sup_1",
                message = "Hello Ambika Precision, can you confirm if you can achieve black anodizing thickness of 15 microns as per drawing note 4?",
                timestamp = System.currentTimeMillis() - 86400000
            ),
            ChatMessageEntity(
                id = "msg_2",
                rfqId = "rfq_101",
                senderId = "usr_sup_1",
                senderName = "Rajesh Sharma",
                receiverId = "usr_buyer_1",
                message = "Yes Mr. Vikram, our in-house anodizing vendor gives certified Mil-A-8625 Type II coating with salt spray testing.",
                timestamp = System.currentTimeMillis() - 82000000
            )
        )
        dao.insertChatMessages(demoChats)

        // Seed Sample Reviews
        val demoReviews = listOf(
            ReviewEntity(
                id = "rev_1",
                orderId = "ord_99",
                supplierId = "usr_sup_1",
                buyerName = "Anil Verma",
                buyerCompany = "Force Motors Vendor",
                rating = 5.0f,
                comment = "Excellent tolerance consistency. Delivered 1,000 VMC blocks with zero rejection in incoming QA inspection.",
                date = "12 Jul 2026"
            ),
            ReviewEntity(
                id = "rev_2",
                orderId = "ord_98",
                supplierId = "usr_sup_2",
                buyerName = "Sunil Gupta",
                buyerCompany = "Havells India Contractor",
                rating = 4.8f,
                comment = "Laser cutting clean edges without burr. Fast dispatch to Delhi NCR site.",
                date = "04 Aug 2026"
            )
        )
        dao.insertReviews(demoReviews)
    }
}
