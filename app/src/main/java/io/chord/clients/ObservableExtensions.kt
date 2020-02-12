package io.chord.clients

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

fun <T : Any> Observable<T>.doOnSuccess(onSuccess: ((T) -> Unit)): Observable<T>
{
	return this.doOnNext(onSuccess)
}

fun <T, R> Observable<T>.concatWith(other: Observable<R>): Observable<R>
{
	return this.concatWith(other)
}