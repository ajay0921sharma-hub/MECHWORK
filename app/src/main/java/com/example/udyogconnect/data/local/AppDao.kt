package com.example.udyogconnect.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // User
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserByIdSync(userId: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("UPDATE users SET verificationStatus = :status WHERE id = :userId")
    suspend fun updateUserVerification(userId: String, status: String)

    // Suppliers
    @Query("SELECT * FROM supplier_profiles")
    fun getAllSupplierProfiles(): Flow<List<SupplierProfileEntity>>

    @Query("SELECT * FROM supplier_profiles WHERE supplierId = :supplierId")
    fun getSupplierProfileById(supplierId: String): Flow<SupplierProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplierProfile(profile: SupplierProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplierProfiles(profiles: List<SupplierProfileEntity>)

    @Query("UPDATE supplier_profiles SET verificationStatus = :status WHERE supplierId = :supplierId")
    suspend fun updateSupplierVerification(supplierId: String, status: String)

    // RFQs
    @Query("SELECT * FROM rfqs ORDER BY createdAt DESC")
    fun getAllRfqs(): Flow<List<RfqEntity>>

    @Query("SELECT * FROM rfqs WHERE buyerId = :buyerId ORDER BY createdAt DESC")
    fun getRfqsByBuyer(buyerId: String): Flow<List<RfqEntity>>

    @Query("SELECT * FROM rfqs WHERE id = :rfqId")
    fun getRfqById(rfqId: String): Flow<RfqEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRfq(rfq: RfqEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRfqs(rfqs: List<RfqEntity>)

    @Query("UPDATE rfqs SET status = :status WHERE id = :rfqId")
    suspend fun updateRfqStatus(rfqId: String, status: String)

    // Quotes
    @Query("SELECT * FROM quotes WHERE rfqId = :rfqId ORDER BY totalPrice ASC")
    fun getQuotesForRfq(rfqId: String): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE supplierId = :supplierId ORDER BY createdAt DESC")
    fun getQuotesBySupplier(supplierId: String): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE id = :quoteId")
    fun getQuoteById(quoteId: String): Flow<QuoteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<QuoteEntity>)

    @Query("UPDATE quotes SET status = :status WHERE id = :quoteId")
    suspend fun updateQuoteStatus(quoteId: String, status: String)

    // Orders
    @Query("SELECT * FROM orders ORDER BY updatedAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE buyerId = :buyerId ORDER BY updatedAt DESC")
    fun getOrdersForBuyer(buyerId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE supplierId = :supplierId ORDER BY updatedAt DESC")
    fun getOrdersForSupplier(supplierId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun getOrderById(orderId: String): Flow<OrderEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Query("UPDATE orders SET status = :status, trackingNotes = :notes, updatedAt = :updatedAt WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String, notes: String, updatedAt: Long = System.currentTimeMillis())

    // Chat
    @Query("SELECT * FROM chat_messages WHERE rfqId = :rfqId ORDER BY timestamp ASC")
    fun getChatMessagesForRfq(rfqId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessages(messages: List<ChatMessageEntity>)

    // Reviews
    @Query("SELECT * FROM reviews WHERE supplierId = :supplierId")
    fun getReviewsForSupplier(supplierId: String): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    // Categories
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)
}
