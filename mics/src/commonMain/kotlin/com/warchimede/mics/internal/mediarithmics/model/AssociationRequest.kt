package com.warchimede.mics.internal.mediarithmics.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AssociationRequest(val identifiers: List<SerializableIdentifier>)