package com.warchimede.mics.model

import com.warchimede.mics.Mics
import io.ktor.util.date.*

class EventBuilder internal constructor(private val name: Name) {

    private val properties: MutableMap<String, PropertyValue> = mutableMapOf()

    var timestamp: ULong = GMTDate().timestamp.toULong()

    fun set(name: String, value: Double) =
        properties.put(name, PropertyValue.Double(value))

    fun set(name: String, value: Float) =
        properties.put(name, PropertyValue.Float(value))

    fun set(name: String, value: ULong) =
        properties.put(name, PropertyValue.Long(value))

    fun set(name: String, value: String) =
        properties.put(name, PropertyValue.String(value))

    fun set(name: String, value: List<String>) =
        properties.put(name, PropertyValue.StringList(value))


    internal fun createEvent() = Event(name, properties, timestamp)
}

suspend fun Mics.postOpenEvent(builder: (EventBuilder) -> Unit) = post(openEvent(builder))

suspend fun Mics.postInstallEvent(builder: (EventBuilder) -> Unit) = post(installEvent(builder))

suspend fun Mics.postPageEvent(builder: (EventBuilder) -> Unit) = post(pageEvent(builder))

suspend fun Mics.postUpdateEvent(builder: (EventBuilder) -> Unit) = post(updateEvent(builder))

fun openEvent(builder: EventBuilder.() -> Unit) =
    EventBuilder(Name.OPEN).apply(builder).createEvent()

fun installEvent(builder: EventBuilder.() -> Unit) =
    EventBuilder(Name.INSTALL).apply(builder).createEvent()

fun pageEvent(builder: EventBuilder.() -> Unit) =
    EventBuilder(Name.PAGE).apply(builder).createEvent()

fun updateEvent(builder: EventBuilder.() -> Unit) =
    EventBuilder(Name.PAGE).apply(builder).createEvent()