package com.example.udyogconnect.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val role: String, // SUPPLIER, BUYER, ADMIN
    val name: String,
    val companyName: String,
    val email: String,
    val phone: String,
    val city: String,
    val state: String,
    val verificationStatus: String,
    val profileImage: String = "",
    val language: String = "ENGLISH",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "supplier_profiles")
data class SupplierProfileEntity(
    @PrimaryKey val supplierId: String,
    val companyName: String,
    val city: String,
    val state: String,
    val verificationStatus: String,
    val machinesJson: String, // JSON list e.g. ["VMC 4-Axis", "CNC Lathe", "Fiber Laser 3kW"]
    val processesJson: String, // JSON list e.g. ["CNC Machining", "Sheet Metal", "Gear Hobbing"]
    val materialsJson: String, // JSON list e.g. ["SS304", "Mild Steel", "Aluminum 6061"]
    val tolerancesJson: String, // e.g. "±0.005 mm"
    val maxCapacityMonthly: String, // e.g. "50,000 pcs"
    val gstNumber: String,
    val msmeUdyamNumber: String,
    val certificationsJson: String, // e.g. ["ISO 9001:2015", "IATF 16949"]
    val factoryPhotosJson: String, // list of photo tags/URLs
    val rating: Float,
    val reviewCount: Int,
    val pastClientsJson: String, // e.g. ["Tata Motors Vendor Tier-2", "L&T Defense", "Mahindra Supplier"]
    val totalOrdersCompleted: Int,
    val responseTimeHours: Int,
    val description: String = ""
)

@Entity(tableName = "rfqs")
data class RfqEntity(
    @PrimaryKey val id: String,
    val buyerId: String,
    val buyerName: String,
    val buyerCompany: String,
    val partName: String,
    val category: String,
    val material: String,
    val processRequired: String,
    val dimensions: String,
    val tolerance: String,
    val quantity: Int,
    val targetUnitPrice: Double,
    val finishCoating: String,
    val deliveryDate: String,
    val drawingFileName: String,
    val notes: String,
    val status: String, // OPEN, IN_REVIEW, AWARDED, CLOSED
    val locationPreference: String = "All India",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey val id: String,
    val rfqId: String,
    val supplierId: String,
    val supplierName: String,
    val supplierCompany: String,
    val supplierLocation: String,
    val verificationStatus: String,
    val unitPrice: Double,
    val toolingCost: Double,
    val moq: Int,
    val gstPercentage: Double,
    val freightCost: Double,
    val totalPrice: Double,
    val leadTimeDays: Int,
    val paymentTerms: String, // e.g. "30% Advance, 70% against Invoice"
    val matchScore: Int, // 0 to 100
    val notes: String,
    val status: String, // SUBMITTED, SHORTLISTED, ACCEPTED, REJECTED
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val rfqId: String,
    val quoteId: String,
    val buyerId: String,
    val buyerCompany: String,
    val supplierId: String,
    val supplierCompany: String,
    val partName: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalAmount: Double,
    val status: String, // OrderStatus
    val poNumber: String,
    val trackingNotes: String,
    val estimatedDeliveryDate: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val rfqId: String,
    val senderId: String,
    val senderName: String,
    val receiverId: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val orderId: String,
    val supplierId: String,
    val buyerName: String,
    val buyerCompany: String,
    val rating: Float,
    val comment: String,
    val date: String = "Today"
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameHi: String,
    val iconName: String = "ic_default",
    val popularProcessesJson: String
)
