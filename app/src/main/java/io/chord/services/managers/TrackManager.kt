package io.chord.services.managers

import io.chord.clients.models.Track

class TrackManager
{
	companion object
	{
		private val manager: ProjectManager.Companion = ProjectManager
		
		fun add(track: Track)
		{
			val project = this.manager.getCurrent()!!.copy()
			val tracks = mutableListOf<Track>()
			tracks.addAll(project.tracks)
			tracks.add(track)
			project.tracks = tracks.toList()
			this.manager.staging(project)
		}
		
		fun update(track: Track)
		{
			val project = this.manager.getCurrent()!!.copy()
			val tracks = mutableListOf<Track>()
			tracks.addAll(project.tracks)
			val index = tracks.indexOf(track)
			tracks[index] = track
			project.tracks = tracks.toList()
			this.manager.staging(project)
		}
		
		fun delete(track: Track)
		{
			val project = this.manager.getCurrent()!!.copy()
			val tracks = mutableListOf<Track>()
			tracks.addAll(project.tracks)
			tracks.remove(track)
			project.tracks = tracks.toList()
			this.manager.staging(project)
		}
	}
}