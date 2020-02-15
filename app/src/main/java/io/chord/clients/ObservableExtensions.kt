package io.chord.clients

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.functions.Functions

fun <T : Any> Observable<T>.observe(): Disposable
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

fun <T : Any> Observable<T>.doOnSuccess(onSuccess: ((T) -> Unit)): Observable<T>
{
	return this.doOnNext(onSuccess)
}

fun Completable.doOnSuccess(onSuccess: (() -> Unit)): Completable
{
	return this.doOnComplete(onSuccess)
}