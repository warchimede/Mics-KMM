package com.warchimede.mics.model

enum class Name {
    INSTALL,
    OPEN,
    PAGE,
    UPDATE
}

sealed interface PropertyValue {
    data class Double(val double: kotlin.Double) : PropertyValue
    data class Float(val float: kotlin.Float) : PropertyValue
    data class Long(val long: ULong) : PropertyValue
    data class StringList(val list: List<kotlin.String>) : PropertyValue
    data class String(val string: kotlin.String) : PropertyValue
}

data class Event(
    val name: Name,
    val properties: Map<String, PropertyValue>,
    val timestamp: ULong
)