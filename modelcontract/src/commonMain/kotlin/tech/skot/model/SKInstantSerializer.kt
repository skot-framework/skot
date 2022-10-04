package tech.skot.model

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.skot.core.SKDateFormat

open class SKInstantSerializer(name:String, pattern:String):KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor(name, PrimitiveKind.STRING)

    private val dateFormat = SKDateFormat(pattern)

    fun serialize(obj: Instant) = dateFormat.format(obj)
    fun parse(str: String) = dateFormat.parse(str)

    override fun serialize(output: Encoder, obj: Instant) {
        output.encodeString(serialize(obj))
    }

    override fun deserialize(input: Decoder): Instant {
        val json = input.decodeString()
        return parse(json)
    }
}