package io.chord.clients.models

import io.chord.clients.models.Object
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
    * @property errors
    * @property type
    * @property title
    * @property status
    * @property detail
    * @property instance
    * @property extensions
*/

open class ValidationProblemDetails(

    @Json(name = "errors") @field:Json(name = "errors") var errors: Map<String, List<String>>? = null,
    @Json(name = "type") @field:Json(name = "type") var type: String? = null,
    @Json(name = "title") @field:Json(name = "title") var title: String? = null,
    @Json(name = "status") @field:Json(name = "status") var status: Int? = null,
    @Json(name = "detail") @field:Json(name = "detail") var detail: String? = null,
    @Json(name = "instance") @field:Json(name = "instance") var instance: String? = null,
    @Json(name = "extensions") @field:Json(name = "extensions") var extensions: Map<String, Object>? = null): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): ValidationProblemDetails
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as ValidationProblemDetails

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
