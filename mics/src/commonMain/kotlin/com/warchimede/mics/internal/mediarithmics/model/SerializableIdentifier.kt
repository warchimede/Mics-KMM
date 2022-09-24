package com.warchimede.mics.internal.mediarithmics.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class SerializableIdentifier

@Serializable
@SerialName("USER_ACCOUNT")
internal data class SerializableAccountIdentifier(
    val compartment_id: String,
    val user_account_id: String
) : SerializableIdentifier()

@Serializable
@SerialName("USER_AGENT")
internal data class SerializableUserAgentIdentifier(val user_agent_id: String) : SerializableIdentifier()