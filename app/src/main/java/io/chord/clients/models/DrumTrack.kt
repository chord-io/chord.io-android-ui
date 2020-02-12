package io.chord.clients.models


import io.chord.clients.models.MidiTrack
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property drumMap
*/
@JsonClass(generateAdapter = true)
open class DrumTrack(
    name: kotlin.String,
    color: kotlin.Int,
    channel: kotlin.Int,
    @Json(name = "drum_map") @field:Json(name = "drum_map") var drumMap: kotlin.String
) : MidiTrack(
    name,
    color,
    channel) 
{
    override fun copy(): DrumTrack
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as DrumTrack
    }
}

