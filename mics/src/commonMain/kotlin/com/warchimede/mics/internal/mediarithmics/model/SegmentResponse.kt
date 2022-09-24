package com.warchimede.mics.internal.mediarithmics.model

import kotlinx.serialization.Serializable

@Serializable
internal data class SegmentResponse(
    val status: String,
    val data: List<SerializableSegment>
)