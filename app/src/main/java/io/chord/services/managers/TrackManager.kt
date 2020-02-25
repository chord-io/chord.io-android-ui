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
			val tracks = project.tracks.toMutableList()
			tracks.add(track)
			project.tracks = tracks.toList()
			this.manager.staging(project)
		}
		
		fun update(index: Int, track: Track)
		{
			val project = this.manager.getCurrent()!!.copy()
			val tracks = project.tracks.toMutableList()
			tracks[index] = track
			project.tracks = tracks.toList()
			this.manager.staging(project)
		}
		
		fun delete(track: Track)
		{
			val project = this.manager.getCurrent()!!.copy()
			val tracks = project.tracks.toMutableList()
			tracks.removeIf {
				it.name == track.name
			}
			project.tracks = tracks.toList()
			this.manager.staging(project)
		}
		
		fun move(from: Int, to: Int)
		{
			if(from == to)
			{
				return
			}
			
			val project = this.manager.getCurrent()!!.copy()
			val tracks = project.tracks.toMutableList()
			val item = tracks[from]
			tracks.removeAt(from)
			tracks.add(to, item)
			project.tracks = tracks.toList()
			this.manager.staging(project)
		}
		
		fun indexOf(track: Track): Int
		{
			val tracks = this.manager.getCurrent()!!.tracks
			return tracks.indexOfFirst {
				it.name == track.name
			}
		}
	}
}