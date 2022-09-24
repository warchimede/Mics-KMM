package com.warchimede.mics.internal.mediarithmics.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
internal data class SerializableEvent(
    @SerialName("\$event_name")
    val name: String,
    @SerialName("\$ts")
    val timestamp: ULong,
    @SerialName("\$properties")
    private var properties: Map<String, JsonElement>
)