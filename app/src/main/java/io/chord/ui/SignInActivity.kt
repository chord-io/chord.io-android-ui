package io.chord.ui

import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.chord.R
import io.chord.client.ClientApi
import io.chord.client.apis.AuthenticationApi
import io.chord.client.apis.UsersApi
import io.chord.client.models.SignInDto
import io.chord.client.models.UserDto
import io.chord.client.toApiThrowable
import io.chord.ui.components.Banner
import io.chord.ui.dialog.FullscreenDialogFragment
import io.chord.ui.extensions.observe
import io.chord.ui.extensions.toBanerApiThrowable
import io.reactivex.android.schedulers.AndroidSchedulers


class SignInActivity : AppCompatActivity()
{
	private val client: AuthenticationApi = ClientApi.getAuthenticationApi()
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_in)
		
		this.findViewById<RelativeLayout>(R.id.signup)
			.setOnClickListener {this.showSignUpDialog()}
		
		this.findViewById<Button>(R.id.signin)
			.setOnClickListener { this.signIn() }
	}
	
	private fun signIn()
	{
		val button = this.findViewById<Button>(R.id.signin)
		val usernameLayout = this.findViewById<TextInputLayout>(R.id.usernameLayout)
		val passwordLayout = this.findViewById<TextInputLayout>(R.id.passwordLayout)
		val username = this.findViewById<TextInputEditText>(R.id.username)
		val password = this.findViewById<TextInputEditText>(R.id.password)
		
		val dto = SignInDto(
			username.text.toString(),
			password.text.toString()
		)
		
		this.client.signIn(dto)
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe {
				usernameLayout.isErrorEnabled = false
				passwordLayout.isErrorEnabled = false
			}
			.doOnSuccess {
				// TODO: save auth
			}
			.doOnError {throwable ->
				throwable
					.toApiThrowable()
					.doOnValidationError { mapper ->
						mapper
							.map("Username") { error ->
								usernameLayout.isErrorEnabled = true
								usernameLayout.error = error
							}
							.map("Password") { error ->
								passwordLayout.isErrorEnabled = true
								passwordLayout.error = error
							}
							.observe()
					}
					.doOnPostObservation {
					}
					.observe()
			}
			.observe()
	}
	
	private fun showSignUpDialog()
	{
		val fragmentManager = this.supportFragmentManager
		val dialogFragment = FullscreenDialogFragment(
			R.layout.sign_up_dialog_form,
			this.getString(R.string.signup_dialog_title)
		)
		
		dialogFragment.onLayoutUpdatedListener = { it ->
			val banner = it!!.findViewById<Banner>(R.id.banner)
			val usernameLayout = it.findViewById<TextInputLayout>(R.id.usernameLayout)
			val emailLayout = it.findViewById<TextInputLayout>(R.id.emailLayout)
			val passwordLayout = it.findViewById<TextInputLayout>(R.id.passwordLayout)
			val username = it.findViewById<TextInputEditText>(R.id.username)
			val email = it.findViewById<TextInputEditText>(R.id.email)
			val password = it.findViewById<TextInputEditText>(R.id.password)
			
			val user = UserDto(
				username.text.toString(),
				email.text.toString(),
				password.text.toString()
			)
			
			this.client.signUp(user)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe {
					banner.dismiss()
					usernameLayout.isErrorEnabled = false
					emailLayout.isErrorEnabled = false
					passwordLayout.isErrorEnabled = false
				}
				.doOnSuccess {
					dialogFragment.validate()
					// TODO save user id
				}
				.doOnError { throwable ->
					throwable
						.toBanerApiThrowable(banner)
						.doOnValidationError { mapper ->
							mapper
								.map("Username") { error ->
									usernameLayout.isErrorEnabled = true
									usernameLayout.error = error
								}
								.map("Email") { error ->
									emailLayout.isErrorEnabled = true
									emailLayout.error = error
								}
								.map("Password") { error ->
									passwordLayout.isErrorEnabled = true
									passwordLayout.error = error
								}
								.observe()
						}
						.doOnPostObservation {
							dialogFragment.unvalidate()
						}
						.observe()
				}
				.observe()
		}
		
		dialogFragment.show(fragmentManager, "fragment_signup_form")
	}
}
