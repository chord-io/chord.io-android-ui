package io.chord.ui

import android.view.View
import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.client.ApiThrowable
import io.chord.client.toApiThrowable
import io.chord.ui.components.Banner
import io.chord.ui.dialog.ErrorDialogFragment

@Suppress("ThrowableNotThrown")
fun Throwable.toActivityApiThrowable(activity: FragmentActivity): ApiThrowable {
	val throwable = this.toApiThrowable()
	
	throwable
		.doOnConnectionTimeout {
			val fragmentManager = activity.supportFragmentManager
			val dialogFragment = ErrorDialogFragment(R.string.api_connection_timeout)
			dialogFragment.show(fragmentManager, "fragment_network_timeout_dialog")
		}
		.doOnNetworkError {
			val fragmentManager = activity.supportFragmentManager
			val dialogFragment = ErrorDialogFragment(R.string.api_network_error)
			dialogFragment.show(fragmentManager, "fragment_network_error_dialog")
		}
	
	return throwable
}

@Suppress("ThrowableNotThrown")
fun Throwable.toBanerApiThrowable(banner: Banner): ApiThrowable {
	val throwable = this.toApiThrowable()
	
	throwable
		.doOnError { _, message ->
			banner.message = message
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