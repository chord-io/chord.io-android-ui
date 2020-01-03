package io.chord.ui

import android.app.Application
import android.content.res.Resources
import io.chord.client.models.Chord
import io.chord.services.depencyinjection.keycloakClient
import io.chord.services.depencyinjection.sharedPreferencesAuthenticationStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChordIOApplication : Application()
{
	companion object Singleton
	{
		lateinit var instance: ChordIOApplication
	}
	
	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@ChordIOApplication)
			modules(sharedPreferencesAuthenticationStorage + keycloakClient)
		}
		
		instance = this
	}
}