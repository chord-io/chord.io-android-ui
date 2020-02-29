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
*/

open class DrumSequence(
    length: SequenceLength,
    fingering: InnerFingering): Serializable, MidiSequence(
    length,
    fingering)
{
    override fun copy(regenerate: Boolean): DrumSequence
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as DrumSequence

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
