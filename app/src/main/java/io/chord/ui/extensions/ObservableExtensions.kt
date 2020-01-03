package io.chord.ui.extensions

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.internal.functions.Functions

fun <T : Any> Single<T>.observe(): Disposable
{
	return this.subscribe(
		Functions.emptyConsumer(),
		Functions.emptyConsumer()
	)
}