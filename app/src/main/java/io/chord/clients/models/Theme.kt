package io.chord.clients.models


import io.chord.clients.models.Sequence
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property name
    * @property trackIndex
    * @property sequences
*/
@JsonClass(generateAdapter = true)
open class Theme(
    @Json(name = "name") @field:Json(name = "name") var name: kotlin.String,
    @Json(name = "track_index") @field:Json(name = "track_index") var trackIndex: kotlin.Int,
    @Json(name = "sequences") @field:Json(name = "sequences") var sequences: kotlin.Array<Sequence>
)
{
    open fun copy(): Theme
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as Theme
    }
}

