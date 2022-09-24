package com.warchimede.mics.model

data class Identifiers(
    val vendorId: AccountIdentifier,
    val customUserId: AccountIdentifier?,
    val advertisingId: String?
)

data class AccountIdentifier(
    val compartmentId: String,
    val identifier: String
)