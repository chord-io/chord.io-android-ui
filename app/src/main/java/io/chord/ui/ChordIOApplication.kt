package io.chord.ui

import android.app.Application
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import io.chord.services.depencyinjection.sharedPreferencesAuthenticationStorage
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class ChordIOApplication : Application()
{
	companion object Singleton
	{
		lateinit var instance: ChordIOApplication
	}
	
	val storage: SharedPreferencesAuthenticationStorage by inject()
	
	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@ChordIOApplication)
			modules(sharedPreferencesAuthenticationStorage)
		}
		
		instance = this
	}
}