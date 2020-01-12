package io.chord.services.managers

import io.chord.client.ClientApi
import io.chord.client.apis.ProjectsApi
import io.chord.client.models.ProjectDto
import io.chord.ui.extensions.observe
import io.reactivex.android.schedulers.AndroidSchedulers

class Manager
{
	companion object Project
	{
		private val client: ProjectsApi = ClientApi.getProjectsApi()
		private lateinit var current: io.chord.client.models.Project
		
		fun getCurrent(): io.chord.client.models.Project
		{
			return this.current
		}
		
		fun setCurrent(project: io.chord.client.models.Project)
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