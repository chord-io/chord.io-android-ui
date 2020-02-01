package io.chord.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import io.chord.R
import io.chord.clients.ClientApi
import io.chord.clients.apis.AuthenticationApi
import io.chord.clients.toApiThrowable
import io.chord.databinding.ActivitySignInBinding
import io.chord.databinding.SignUpDialogFormBinding
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import io.chord.services.authentication.workers.RefreshTokenWorker
import io.chord.ui.dialogs.FullscreenDialogFragment
import io.chord.ui.extensions.getRootView
import io.chord.ui.extensions.observe
import io.chord.ui.extensions.setViewState
import io.chord.ui.extensions.toBanerApiThrowable
import io.chord.ui.models.SignUpDialogFormViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject


class SignInActivity : AppCompatActivity()
{
	private val client: AuthenticationApi = ClientApi.getAuthenticationApi()
	private val storage: SharedPreferencesAuthenticationStorage by inject()
	private lateinit var binding: ActivitySignInBinding
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_in)
		
		this.binding = DataBindingUtil.bind(this.getRootView())!!
		
		this.findViewById<RelativeLayout>(R.id.signup)
			.setOnClickListener {this.signUp()}
		
		this.findViewById<Button>(R.id.signin)
			.setOnClickListener { this.signIn() }
	}
	
	private fun signIn()
	{
		this.bindProgressButton(this.binding.signin)
		this.binding.signin.attachTextChangeAnimator()

		val dto = this.binding.user!!.toDto()

		this.client.signIn(dto)
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe {
				this.binding.signin.showProgress {
					progressColor = R.color.backgroundPrimary
				}
				this.binding.layout.setViewState(false)
				this.binding.usernameLayout.isErrorEnabled = false
				this.binding.passwordLayout.isErrorEnabled = false
			}
			.doOnSuccess {authentication ->
				this.storage.store(authentication)
				RefreshTokenWorker.start(R.integer.refresh_token_worker_interval.toLong())
				this.startActivity(Intent(
					this,
					MainActivity::class.java
				))
			}
			.doOnError {throwable ->
				throwable
					.toApiThrowable()
					.doOnValidationError { mapper ->
						mapper
							.map("Username") { error ->
								this.binding.usernameLayout.isErrorEnabled = true
								this.binding.usernameLayout.error = error
							}
							.map("Password") { error ->
								this.binding.passwordLayout.isErrorEnabled = true
								this.binding.passwordLayout.error = error
							}
							.observe()
					}
					.doOnPostObservation {
						this.binding.signin.hideProgress(R.string.signin_signin)
						this.binding.layout.setViewState(true)
					}
					.observe()
			}
			.observe()
	}
	
	private fun signUp()
	{
		val fragmentManager = this.supportFragmentManager
		val dialogFragment = FullscreenDialogFragment<SignUpDialogFormBinding>(
			R.layout.sign_up_dialog_form,
			this.getString(R.string.signup_dialog_title)
		)
		
		dialogFragment.onViewModelBinding = {
			it.user = SignUpDialogFormViewModel()
		}
		
		dialogFragment.onLayoutUpdatedListener = { binding: SignUpDialogFormBinding ->
			val userToCreate = binding.user!!.toDto()

			this.client.signUp(userToCreate)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe {
					dialogFragment.banner.dismiss()
					binding.usernameLayout.isErrorEnabled = false
					binding.emailLayout.isErrorEnabled = false
					binding.passwordLayout.isErrorEnabled = false
				}
				.doOnSuccess {
					dialogFragment.validate()
				}
				.doOnError { throwable ->
					throwable
						.toBanerApiThrowable(dialogFragment.banner)
						.doOnValidationError { mapper ->
							mapper
								.map("Username") { error ->
									binding.usernameLayout.isErrorEnabled = true
									binding.usernameLayout.error = error
								}
								.map("Email") { error ->
									binding.emailLayout.isErrorEnabled = true
									binding.emailLayout.error = error
								}
								.map("Password") { error ->
									binding.passwordLayout.isErrorEnabled = true
									binding.passwordLayout.error = error
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
