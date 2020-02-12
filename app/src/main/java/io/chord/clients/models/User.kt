package io.chord.clients.models

import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property id
    * @property username
    * @property email
*/
@JsonClass(generateAdapter = true)
open class User(
    @Json(name = "id") @field:Json(name = "id") var id: kotlin.String,
    @Json(name = "username") @field:Json(name = "username") var username: kotlin.String,
    @Json(name = "email") @field:Json(name = "email") var email: kotlin.String
)
{
    open fun copy(): User
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as User
    }
}

