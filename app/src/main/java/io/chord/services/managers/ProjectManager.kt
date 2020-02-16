package io.chord.services.managers

import io.chord.clients.ClientApi
import io.chord.clients.apis.ProjectsApi
import io.chord.clients.doOnSuccess
import io.chord.clients.models.Project
import io.chord.clients.models.ProjectDto
import io.chord.clients.observe
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
			val observable = PublishSubject.create<Project>()
			val projectToCreate = this.toDto(project)
			return this.client.create(projectToCreate)
				.observeOn(AndroidSchedulers.mainThread())
				.concatWith(observable)
		}
		
		fun delete(project: Project): Observable<Project>
		{
			this.deleteListeners
				.removeIf {
					it == null
				}
			val observable = PublishSubject.create<Project>()
			this.client.delete(project.id)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSuccess {
					observable.onNext(project)
					if(project.id == this.current?.id)
					{
						this.current = null
						this.stage = null
						this.deleteListeners.forEach {
							it?.invoke()
						}
					}
				}
				.observe()
			return observable
		}
		
		fun update(): Observable<Project>
		{
			val project = if(this.stage != null) this.stage!! else this.current
			return this.update(project!!)
		}
		
		fun update(project: Project): Observable<Project>
		{
			this.updateListeners
				.removeIf {
					it == null
				}
			val projectToUpdate = this.toDto(project)
			val observable = PublishSubject.create<Project>()
			this.client.update(project.id, projectToUpdate)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSuccess {
					observable.onNext(project)
					if(project.id == this.current?.id)
					{
						this.current = project
						this.stage = null
						this.updateListeners.forEach {
							it?.invoke()
						}
					}
				}
				.doOnError {
					observable.onError(it)
					this.stage = null
				}
				.observe()
			return observable
		}
		
		fun getAllByAuthor(): Observable<List<Project>>
		{
			val observable = PublishSubject.create<List<Project>>()
			return this.client.allByAuthor
				.observeOn(AndroidSchedulers.mainThread())
				.concatWith(observable)
		}
	}
}