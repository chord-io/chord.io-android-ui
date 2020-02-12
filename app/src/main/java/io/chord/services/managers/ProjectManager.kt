package io.chord.services.managers

import io.chord.clients.ClientApi
import io.chord.clients.apis.ProjectsApi
import io.chord.clients.doOnSuccess
import io.chord.clients.models.Project
import io.chord.clients.models.ProjectDto
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class ProjectManager
{
	companion object
	{
		private val client: ProjectsApi = ClientApi.getProjectsApi()
		private var current: Project? = null
		private var stage: Project? = null
		private val updateListeners: MutableList<(() -> Unit)?> = mutableListOf()
		private val deleteListeners: MutableList<(() -> Unit)?> = mutableListOf()
		
		val tracks: TrackManager.Companion = TrackManager.Companion
		
		private fun toDto(project: Project): ProjectDto
		{
			return ProjectDto(
				project.name,
				project.tempo,
				project.visibility,
				project.tracks,
				project.themes
			)
		}
		
		fun addOnUpdateListener(listener: (() -> Unit)?)
		{
			this.updateListeners.add(listener)
		}
		
		fun addOnDeleteListener(listener: (() -> Unit)?)
		{
			this.deleteListeners.add(listener)
		}
		
		fun getCurrent(): Project?
		{
			return this.current
		}
		
		fun setCurrent(project: Project)
		{
			this.current = project
			this.stage = null
		}
		
		fun staging(project: Project)
		{
			this.stage = project
		}
		
		fun create(project: Project): Observable<Project>
		{
			val observable = Observable.just(project)
			val projectToCreate = this.toDto(project)
			return this.client.create(projectToCreate)
				.observeOn(AndroidSchedulers.mainThread())
				.concatWith(observable)
		}
		
		fun delete(project: Project): Observable<Void>
		{
			this.deleteListeners
				.removeIf {
					it == null
				}
			
			val observable = Observable.empty<Void>()
			return this.client.delete(project.id)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSuccess {
					if(project.id == this.current?.id)
					{
						this.current = null
						this.stage = null
						this.deleteListeners.forEach {
							it?.invoke()
						}
					}
				}
				.concatWith(observable)
		}
		
		fun update(): Observable<Void>
		{
			val project = if(this.stage != null) this.stage!! else this.current
			return this.update(project!!)
		}
		
		fun update(project: Project): Observable<Void>
		{
			this.updateListeners
				.removeIf {
					it == null
				}
			
			val observable = Observable.empty<Void>()
			val projectToUpdate = ProjectDto(
				project.name,
				project.tempo,
				project.visibility,
				project.tracks,
				project.themes
			)
			return this.client.update(this.current!!.id, projectToUpdate)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSuccess {
					this.current = project
					this.stage = null
					this.updateListeners.forEach {
						it?.invoke()
					}
				}
				.doOnError {
					this.stage = null
				}
				.concatWith(observable)
		}
		
		fun getAllByAuthor(): Observable<List<Project>>
		{
			val observable = PublishSubject.create<List<Project>>()
			return this.client.allByAuthor
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSuccess {
					observable.onNext(it)
				}
				.concatWith(observable)
		}
	}
}