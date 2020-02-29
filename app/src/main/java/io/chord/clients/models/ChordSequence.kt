package io.chord.clients.models

import io.chord.clients.models.InnerFingering
import io.chord.clients.models.MidiSequence
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
    * @property root
    * @property intervals
*/

open class ChordSequence(
    length: SequenceLength,
    fingering: InnerFingering,
    @Json(name = "root") @field:Json(name = "root") var root: Int,
    @Json(name = "intervals") @field:Json(name = "intervals") var intervals: List<String>
): Serializable, MidiSequence(
    length,
    fingering)
{
    override fun copy(regenerate: Boolean): ChordSequence
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as ChordSequence

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
