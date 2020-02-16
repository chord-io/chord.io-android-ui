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
		
		fun update(index: Int, track: Track)
		{
			val project = this.manager.getCurrent()!!.copy()
			val tracks = mutableListOf<Track>()
			tracks.addAll(project.tracks)
			tracks[index] = track
			project.tracks = tracks.toList()
			this.manager.staging(project)
		}
		
		fun delete(track: Track)
		{
			val project = this.manager.getCurrent()!!.copy()
			val tracks = mutableListOf<Track>()
			tracks.addAll(project.tracks)
			tracks.remove(this.getTrack(tracks, track))
			project.tracks = tracks.toList()
			this.manager.staging(project)
		}
		
		fun indexOf(track: Track): Int
		{
			val tracks = this.manager.getCurrent()!!.tracks
			return this.indexOf(tracks, track)
		}
		
		private fun getTrack(tracks: List<Track>, track: Track): Track?
		{
			return tracks.filter {
				it.name == track.name
			}
			.firstOrNull()
		}
		
		private fun indexOf(tracks: List<Track>, track: Track): Int
		{
			val item = this.getTrack(tracks, track)
			
			if(item != null)
			{
				return tracks.indexOf(item)
			}
			
			return -1
		}
	}
}