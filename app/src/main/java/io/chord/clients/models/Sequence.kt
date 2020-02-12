package io.chord.clients.models


import io.chord.clients.models.SequenceLength
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property length
*/
@JsonClass(generateAdapter = true)
abstract class Sequence(
    @Json(name = "length") @field:Json(name = "length") var length: SequenceLength
)
{
    open fun copy(): Sequence
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as Sequence
    }
}
