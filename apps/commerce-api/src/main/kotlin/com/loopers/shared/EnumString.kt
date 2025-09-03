package com.loopers.shared

interface EnumString<T : Enum<T>> {
    val name: String
}

inline fun <reified T : Enum<T>> EnumString<T>.fromEnum(enum: T): EnumString<T> = object : EnumString<T> {
    override val name: String
        get() = enum.name
}

inline fun <reified T> EnumString<T>.enumFrom(value: String): T where T : Enum<T>, T : EnumString<T> =
    enumValues<T>().firstOrNull {
        it.name.equals(value, ignoreCase = true)
    }
        ?: throw IllegalArgumentException("Unknown enum value: $value")
