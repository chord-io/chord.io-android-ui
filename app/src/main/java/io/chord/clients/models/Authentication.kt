package io.chord.clients.models

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
    * @property accessToken
    * @property refreshToken
    * @property expiresIn
    * @property refreshExpiresIn
    * @property expirationDate
    * @property refreshExpirationDate
*/

open class Authentication(

    @Json(name = "access_token") @field:Json(name = "access_token") var accessToken: String? = null,
    @Json(name = "refresh_token") @field:Json(name = "refresh_token") var refreshToken: String? = null,
    @Json(name = "expires_in") @field:Json(name = "expires_in") var expiresIn: Int? = null,
    @Json(name = "refresh_expires_in") @field:Json(name = "refresh_expires_in") var refreshExpiresIn: Int? = null,
    @Json(name = "expiration_date") @field:Json(name = "expiration_date") var expirationDate: org.threeten.bp.ZonedDateTime? = null,
    @Json(name = "refresh_expiration_date") @field:Json(name = "refresh_expiration_date") var refreshExpirationDate: org.threeten.bp.ZonedDateTime? = null): Serializable, BaseModel()
{
    open fun copy(): Authentication
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as Authentication
    }
}
