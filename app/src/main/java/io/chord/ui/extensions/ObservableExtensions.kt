package io.chord.ui.extensions

import io.reactivex.Completable
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

fun Completable.observe(): Disposable
{
	return this.subscribe(
		Functions.EMPTY_ACTION,
		Functions.emptyConsumer()
	)
}