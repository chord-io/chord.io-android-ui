package io.chord.ui.dialogs.flows

import io.reactivex.Observable

interface CudcFlow<T> : Flow
{
	fun create(): Observable<T>
	fun update(model: T): Observable<T>
	fun delete(model: T): Observable<Void>
	fun clone(model: T): Observable<T>
}