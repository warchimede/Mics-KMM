package com.warchimede.mics.internal.mapping

import com.warchimede.mics.internal.mediarithmics.model.*
import com.warchimede.mics.internal.mediarithmics.model.SerializableAccountIdentifier
import com.warchimede.mics.internal.mediarithmics.model.SerializableEvent
import com.warchimede.mics.internal.mediarithmics.model.SerializableIdentifier
import com.warchimede.mics.internal.mediarithmics.model.SerializableUserAgentIdentifier
import com.warchimede.mics.internal.mediarithmics.model.UserActivity
import com.warchimede.mics.model.Identifiers
import com.warchimede.mics.model.Event
import com.warchimede.mics.model.Name
import com.warchimede.mics.model.PropertyValue
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

internal class RequestMapper(
    private val platformId: String,
    private val appId: String
) {

    fun createAssociationRequest(identifiers: Identifiers): AssociationRequest {
        val ids = prioritizedSerializableIds(identifiers)
        require(ids.size > 1) { "Nothing to reconcile. At least two ids are required" }
        return AssociationRequest(ids)
    }

    fun createUserActivityRequest(event: Event, identifiers: Identifiers): UserActivity {
        val id  = prioritizedSerializableIds(identifiers).filterIsInstance<SerializableAccountIdentifier>().first()
        return UserActivity(
            appId = appId,
            compartmentId = id.compartment_id,
            userAccountId = id.user_account_id,
            timestamp = event.timestamp,
            userAgentId = identifiers.advertisingId,
            events = listOf(event.toSerializableEvent())
        )
    }

    fun createSegmentRequest(identifiers: Identifiers): SerializableIdentifier =
        prioritizedSerializableIds(identifiers).first()

    private fun prioritizedSerializableIds(identifiers: Identifiers): List<SerializableIdentifier> = listOfNotNull(
        identifiers.advertisingId?.let { SerializableUserAgentIdentifier(formatAdvertisingId(it)) },
        identifiers.customUserId?.let { SerializableAccountIdentifier(it.compartmentId, it.identifier) },
        SerializableAccountIdentifier(identifiers.vendorId.compartmentId, identifiers.vendorId.identifier)
    )

    private fun formatAdvertisingId(advertisingId: String): String =
        "mob:${platformId}:raw:$advertisingId"
}

private fun Event.toSerializableEvent() = SerializableEvent(
    name.toSerializable(),
    timestamp,
    properties.mapValues { (_, value) -> value.toSerializable() }
)

private fun PropertyValue.toSerializable(): JsonElement = when (this) {
    is PropertyValue.Double -> JsonPrimitive(double)
    is PropertyValue.StringList -> JsonArray(list.map { JsonPrimitive(it) })
    is PropertyValue.Float -> JsonPrimitive(float)
    is PropertyValue.Long -> JsonPrimitive(long.toLong())
    is PropertyValue.String -> JsonPrimitive(string)
}

private fun Name.toSerializable(): String = when (this) {
    Name.INSTALL -> "\$app_install"
    Name.OPEN -> "\$app_open"
    Name.PAGE -> "page_view" // no \$ for this one. This is intended
    Name.UPDATE -> "\$app_update"
}