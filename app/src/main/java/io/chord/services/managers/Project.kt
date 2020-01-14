package io.chord.services.managers

import io.chord.clients.ClientApi
import io.chord.clients.apis.ProjectsApi
import io.chord.clients.models.ProjectDto
import io.chord.ui.extensions.observe
import io.reactivex.android.schedulers.AndroidSchedulers

class Manager
{
	companion object Project
	{
		private val client: ProjectsApi = ClientApi.getProjectsApi()
		private lateinit var current: io.chord.clients.models.Project
		
		fun getCurrent(): io.chord.clients.models.Project
		{
			return this.current
		}
		
		fun setCurrent(project: io.chord.clients.models.Project)
		{
			this.current = project
		}
		
		fun save()
		{
			val projectToUpdate = ProjectDto(
				this.current.name,
				this.current.tempo,
				this.current.isPrivate,
				this.current.tracks
			)
			this.client.update(this.current.id, projectToUpdate)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnError {
					// TODO: do something on errors
				}
				.observe()
		}
	}
}