package io.chord.services.managers

import io.chord.clients.models.Theme

class ThemeManager
{
	companion object
	{
		private val manager: ProjectManager.Companion = ProjectManager
		
		fun add(theme: Theme)
		{
			val project = this.manager.getCurrent()!!.copy()
			val tracks = project.tracks
			tracks.forEach { track ->
				val themes = track.themes.toMutableList()
				themes.add(theme.copy())
				track.themes = themes.toList()
			}
			this.manager.staging(project)
		}
		
		fun update(index: Int, theme: Theme)
		{
			val project = this.manager.getCurrent()!!.copy()
			val tracks = project.tracks
			tracks.forEach { track ->
				val themes = track.themes.toMutableList()
				themes[index] = theme.copy()
				track.themes = themes.toList()
			}
			this.manager.staging(project)
		}
		
		fun delete(theme: Theme)
		{
			val project = this.manager.getCurrent()!!.copy()
			val tracks = project.tracks
			tracks.forEach { track ->
				val themes = track.themes.toMutableList()
				themes.removeIf {
					it.name == theme.name
				}
				track.themes = themes.toList()
			}
			this.manager.staging(project)
		}
		
		fun move(from: Int, to: Int)
		{
			if(from == to)
			{
				return
			}
			
			val project = this.manager.getCurrent()!!.copy()
			val tracks = project.tracks
			tracks.forEach { track ->
				val themes = track.themes.toMutableList()
				val item = themes[from]
				themes.removeAt(from)
				themes.add(to, item)
				track.themes = themes.toList()
			}
			this.manager.staging(project)
		}
		
		fun indexOf(theme: Theme): Int
		{
			val track = this.manager.getCurrent()!!.tracks.first()
			return track.themes.indexOfFirst {
				it.name == theme.name
			}
		}
	}
}