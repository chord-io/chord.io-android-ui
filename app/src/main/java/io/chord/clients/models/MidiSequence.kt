package io.chord.clients.models


import io.chord.clients.models.InnerFingering
import io.chord.clients.models.Sequence
import io.chord.clients.models.SequenceLength
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property fingering
*/
@JsonClass(generateAdapter = true)
open class MidiSequence(
    length: SequenceLength,
    @Json(name = "fingering") @field:Json(name = "fingering") var fingering: InnerFingering
) : Sequence(
    length) 
{
    override fun copy(): MidiSequence
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as MidiSequence
    }
}

