package io.chord.services.depencyinjection

import android.content.Context
import com.google.gson.GsonBuilder
import io.chord.R
import io.chord.services.authentication.api.KeycloakClient
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import org.koin.dsl.module

val sharedPreferencesAuthenticationStorage = module {
	fun prefs(context: Context) = context.getSharedPreferences(
		R.string.preference_file_key.toString(),
		Context.MODE_PRIVATE
	)!!
	single { prefs(get()) }
	single { GsonBuilder().setLenient().create() }
	single {
		SharedPreferencesAuthenticationStorage(
			get(),
			get()
		)
	}
}

val keycloakClient = module {
	single { KeycloakClient() }
}