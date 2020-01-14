package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.*

class SignInFormViewModel(
) : BaseObservable()
{
	var username: String = ""
	
	var password: String = ""
	
	fun toDto(): SignInDto
	{
		return SignInDto(
			this.username,
			this.password
		)
	}
}