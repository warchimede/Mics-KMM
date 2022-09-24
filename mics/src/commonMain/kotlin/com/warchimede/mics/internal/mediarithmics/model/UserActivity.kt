package com.warchimede.mics.internal.mediarithmics.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserActivity(
    @SerialName("\$app_id")
    val appId: String,
    @SerialName("\$compartment_id")
    val compartmentId: String,
    @SerialName("\$events")
    val events: List<SerializableEvent>,
    @SerialName("\$ts")
    val timestamp: ULong,
    @SerialName("\$user_account_id")
    val userAccountId: String,
    @SerialName("\$user_agent_id")
    val userAgentId: String?,
    @SerialName("\$session_status")
    val sessionStatus: String = "IN_SESSION",
    @SerialName("\$type")
    val type: String = "APP_VISIT"
)