package io.chord.clients.models

import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property bar
    * @property start
    * @property end
*/
@JsonClass(generateAdapter = true)
open class SequenceLength(
    @Json(name = "bar") @field:Json(name = "bar") var bar: kotlin.Int,
    @Json(name = "start") @field:Json(name = "start") var start: kotlin.Double,
    @Json(name = "end") @field:Json(name = "end") var end: kotlin.Double
)
{
    open fun copy(): SequenceLength
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as SequenceLength
    }
}

