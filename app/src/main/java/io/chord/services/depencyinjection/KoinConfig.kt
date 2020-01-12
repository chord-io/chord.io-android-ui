package io.chord.services.depencyinjection

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.chord.R
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import org.koin.dsl.module
import org.threeten.bp.ZonedDateTime

val sharedPreferencesAuthenticationStorage = module {
	fun prefs(context: Context) = context.getSharedPreferences(
		R.string.preference_file_key.toString(),
		Context.MODE_PRIVATE
	)!!
	single { prefs(get()) }
	single {
		GsonBuilder()
			.setLenient()
			.registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeTypeAdapter())
			.create()
	}
	single {
		SharedPreferencesAuthenticationStorage(
			get() as SharedPreferences,
			get() as Gson
		)
	}
}