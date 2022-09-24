package com.warchimede.mics.internal

import com.warchimede.mics.internal.mediarithmics.model.SerializableEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializationTest {

    @Test
    fun `serializable event with double`() {
        val value = 1.0
        val event = createSinglePropertyEvent(JsonPrimitive(value))

        verifySinglePropertySerialization("$value", event)
    }

    @Test
    fun `serializable event with float`() {
        val value = 1.0f
        val event = createSinglePropertyEvent(JsonPrimitive(value))

        verifySinglePropertySerialization("$value", event)
    }

    @Test
    fun `serializable event with long`() {
        val value = 1L
        val event = createSinglePropertyEvent(JsonPrimitive(value))

        verifySinglePropertySerialization("$value", event)
    }

    @Test
    fun `serializable event with string list`() {
        val event = createSinglePropertyEvent(JsonArray(listOf(JsonPrimitive("hello"))))

        verifySinglePropertySerialization("[\"hello\"]", event)
    }

    @Test
    fun `serializable event with string `() {
        val event = createSinglePropertyEvent(JsonPrimitive("hello"))

        verifySinglePropertySerialization("\"hello\"", event)
    }

    private fun verifySinglePropertySerialization(value: String, event: SerializableEvent) {
        val result = Json.encodeToString(SerializableEvent.serializer(), event)
        assertEquals(createJsonString(value), result)
    }

    private fun createSinglePropertyEvent(jsonElement: JsonElement) = SerializableEvent(
        "hello",
        20L.toULong(),
        mapOf("property" to jsonElement)
    )

    private fun createJsonString(propertyValue: String) = "{\"\$event_name\":\"hello\",\"\$ts\":20,\"\$properties\":{\"property\":$propertyValue}}"
}