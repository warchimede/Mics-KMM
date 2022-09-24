package com.warchimede.mics.internal.mapping

import com.warchimede.mics.internal.mediarithmics.model.AssociationRequest
import com.warchimede.mics.internal.mediarithmics.model.SerializableAccountIdentifier
import com.warchimede.mics.internal.mediarithmics.model.SerializableEvent
import com.warchimede.mics.internal.mediarithmics.model.SerializableUserAgentIdentifier
import com.warchimede.mics.model.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class RequestMapperTest {

    private val vendorId = AccountIdentifier("1000", "vendorId")
    private val customUserId = AccountIdentifier("2000", "customUserId")
    private val defaultIds = Identifiers(
        vendorId,
        customUserId,
        "advertisingId"
    )

    private val requestMapper = RequestMapper("platform", "app")

    @Test
    fun `create association with only vendorId`() {
        val ids = defaultIds.copy(advertisingId = null, customUserId = null)
        assertFails {
            requestMapper().createAssociationRequest(ids)
        }
    }

    @Test
    fun `create association nominal`() {
        val request = requestMapper().createAssociationRequest(defaultIds)

        val expected = AssociationRequest(
            listOf(
                SerializableUserAgentIdentifier("mob:system:raw:advertisingId"),
                SerializableAccountIdentifier("2000", "customUserId"),
                SerializableAccountIdentifier("1000", "vendorId"),
            )
        )

        assertEquals(expected, request)
    }

    @Test
    fun `check complex event serialization`() {
        val event = Event(
            name = Name.OPEN,
            properties = mapOf(
                "double" to PropertyValue.Double(1.0),
                "float" to PropertyValue.Float(1f),
                "long" to PropertyValue.Long(1.toULong()),
                "string" to PropertyValue.String("hello"),
                "stringList" to PropertyValue.StringList(listOf("hel", "lo"))
            ),
            timestamp = ULong.MIN_VALUE
        )
        val serializedEvents = requestMapper.createUserActivityRequest(event, defaultIds).events

        val expected = SerializableEvent(
            "\$app_open",
            0.toULong(),
            mapOf(
                "double" to JsonPrimitive(1.0),
                "float" to JsonPrimitive(1f),
                "long" to JsonPrimitive(1L),
                "string" to JsonPrimitive("hello"),
                "stringList" to JsonArray(listOf(JsonPrimitive("hel"), JsonPrimitive("lo")))
            )
        )
        assertEquals(listOf(expected), serializedEvents)
    }

    @Test
    fun `create segments request advertisingId`() {
        val result = requestMapper.createSegmentRequest(defaultIds)

        val expected = SerializableUserAgentIdentifier("mob:platform:raw:advertisingId")

        assertEquals(expected, result)
    }

    @Test
    fun `create segments request customUserId`() {
        val result = requestMapper.createSegmentRequest(Identifiers(vendorId, customUserId, null))

        val expected = SerializableAccountIdentifier("2000", "customUserId")

        assertEquals(expected, result)
    }

    @Test
    fun `create segments request vendorId`() {
        val result = requestMapper.createSegmentRequest(Identifiers(vendorId, null, null))

        val expected = SerializableAccountIdentifier("1000", "vendorId")

        assertEquals(expected, result)
    }

    private fun requestMapper(platformId: String = "system") = RequestMapper(platformId, "appId")
}