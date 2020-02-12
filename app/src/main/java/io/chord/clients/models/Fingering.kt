package io.chord.clients.models


import io.chord.clients.models.FingeringEntry
import io.chord.clients.models.FingeringType
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property id
    * @property editedFrom
    * @property name
    * @property type
    * @property tags
    * @property evaluations
    * @property entries
*/
@JsonClass(generateAdapter = true)
open class Fingering(
    @Json(name = "id") @field:Json(name = "id") var id: kotlin.String,
    @Json(name = "edited_from") @field:Json(name = "edited_from") var editedFrom: kotlin.String,
    @Json(name = "name") @field:Json(name = "name") var name: kotlin.String,
    @Json(name = "type") @field:Json(name = "type") var type: FingeringType,
    @Json(name = "tags") @field:Json(name = "tags") var tags: kotlin.Array<kotlin.String>,
    @Json(name = "evaluations") @field:Json(name = "evaluations") var evaluations: kotlin.collections.Map<kotlin.String, kotlin.Int>,
    @Json(name = "entries") @field:Json(name = "entries") var entries: kotlin.Array<FingeringEntry>
)
{
    open fun copy(): Fingering
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as Fingering
    }
}

