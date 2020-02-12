package io.chord.ui.extensions

import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.clients.ApiThrowable
import io.chord.clients.toApiThrowable
import io.chord.ui.components.Banner
import io.chord.ui.dialogs.DialogLevel
import io.chord.ui.dialogs.DialogParameters
import io.chord.ui.dialogs.FloatDialogFragment

@Suppress("ThrowableNotThrown")
fun Throwable.toActivityApiThrowable(activity: FragmentActivity): ApiThrowable {
	val throwable = this.toApiThrowable()
	
	throwable
		.doOnConnectionTimeout {
			val fragmentManager = activity.supportFragmentManager
			val dialog = FloatDialogFragment(
				DialogParameters(
					activity,
					R.string.api_connection_timeout,
					null,
					R.string.dialog_positive_button,
					R.string.dialog_negative_button,
					DialogLevel.Error
				)
			)
			dialog.show(fragmentManager, "fragment_connection_error_dialog")
		}
		.doOnNetworkError {
			val fragmentManager = activity.supportFragmentManager
			val dialog = FloatDialogFragment(
				DialogParameters(
					activity,
					R.string.api_network_error,
					null,
					R.string.dialog_positive_button,
					R.string.dialog_negative_button,
					DialogLevel.Error
				)
			)
			dialog.show(fragmentManager, "fragment_network_error_dialog")
		}
	
	return throwable
}

@Suppress("ThrowableNotThrown")
fun Throwable.toBanerApiThrowable(banner: Banner): ApiThrowable {
	val throwable = this.toApiThrowable()
	
	throwable
		.doOnError { code, message ->
			if(code in 500..599)
			{
				banner.message = banner.resources.getString(R.string.api_server_error)
			}
			else
			{
				banner.message = message
			}
			
			banner.show()
		}
		.doOnConnectionTimeout {
			banner.messageId = R.string.api_connection_timeout
			banner.show()
		}
		.doOnNetworkError {
			banner.messageId = R.string.api_network_error
			banner.show()
		}
	
	return throwable
}