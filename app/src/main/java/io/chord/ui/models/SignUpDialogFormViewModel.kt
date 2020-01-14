package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.UserDto

class SignUpDialogFormViewModel(
) : BaseObservable()
{
	var username: String = ""
	
	var email: String = ""
	
	var password: String = ""
	
	fun toDto(): UserDto
	{
		return UserDto(
			this.username,
			this.email,
			this.password
		)
	}
}