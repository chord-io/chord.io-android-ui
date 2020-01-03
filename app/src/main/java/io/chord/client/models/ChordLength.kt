/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: chord.io
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package io.chord.client.models

import com.squareup.moshi.Json

/**
 * 
 * @property bar 
 * @property start 
 * @property end 
 */
data class ChordLength (
        @Json(name = "bar") @field:Json(name = "bar") var bar: Int,
        @Json(name = "start") @field:Json(name = "start") var start: Double,
        @Json(name = "end") @field:Json(name = "end") var end: Double
)

