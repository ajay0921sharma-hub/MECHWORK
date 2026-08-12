package com.example.udyogconnect.data.model

enum class UserRole(val labelEn: String, val labelHi: String) {
    SUPPLIER("Manufacturer / Workshop", "निर्माता / कार्यशाला"),
    BUYER("OEM / Industrial Buyer", "खरीदार / कंपनी"),
    ADMIN("Platform Admin", "एडमिन")
}

enum class VerificationStatus(val labelEn: String, val labelHi: String, val badgeColorHex: Long) {
    VERIFIED_GST_MSME("GST & MSME Verified", "जीएसटी एवं एमएसएमई सत्यापित", 0xFF16A34A),
    GST_ONLY("GST Verified", "जीएसटी सत्यापित", 0xFF2563EB),
    PENDING_VERIFICATION("Verification Pending", "सत्यापन लंबित", 0xFFD97706),
    UNVERIFIED("Unverified", "असत्यापित", 0xFF64748B)
}

enum class RfqStatus(val labelEn: String, val labelHi: String) {
    OPEN("Open for Bids", "निविदाओं के लिए खुला"),
    IN_REVIEW("Quotes Under Review", "समीक्षाधीन दरें"),
    AWARDED("Quote Awarded", "स्वीकृत"),
    COMPLETED("Fulfilled", "पूर्ण"),
    CLOSED("Closed", "बंद")
}

enum class QuoteStatus(val labelEn: String, val labelHi: String) {
    SUBMITTED("Submitted", "जमा की गई"),
    SHORTLISTED("Shortlisted", "शॉर्टलिस्टेड"),
    ACCEPTED("Accepted", "स्वीकृत"),
    REJECTED("Declined", "अस्वीकृत")
}

enum class OrderStatus(val labelEn: String, val labelHi: String, val stepIndex: Int) {
    QUOTE_ACCEPTED("Quote Accepted", "कोटेशन स्वीकृत", 1),
    PO_ISSUED("PO Issued", "क्रय आदेश जारी", 2),
    IN_PRODUCTION("In Production", "उत्पादन चालू", 3),
    QUALITY_CHECK("Quality Inspection (CMM)", "गुणवत्ता जांच (CMM)", 4),
    DISPATCHED("Dispatched", "प्रेषित/रवाना", 5),
    DELIVERED("Delivered", "वितरित", 6),
    COMPLETED("Completed & Reviewed", "पूर्ण और समीक्षित", 7)
}

enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "हिंदी")
}
