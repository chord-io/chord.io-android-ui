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
import java.io.Serializable
import io.chord.clients.BaseModel

/**
    * @property fingering
*/

open class MidiSequence(
    length: SequenceLength,
    @Json(name = "fingering") @field:Json(name = "fingering") var fingering: InnerFingering
): Serializable, Sequence(
    length)
{
    override fun copy(regenerate: Boolean): MidiSequence
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as MidiSequence

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
