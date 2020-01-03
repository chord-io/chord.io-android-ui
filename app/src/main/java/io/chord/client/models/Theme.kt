/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: chord.io
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package io.chord.client.models

import com.squareup.moshi.Json

/**
 * 
 * @property name 
 * @property chords 
 */
data class Theme (
        @Json(name = "name") @field:Json(name = "name") var name: String,
        @Json(name = "chords") @field:Json(name = "chords") var chords: List<Chord>
)

