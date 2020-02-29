package io.chord.ui.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorRes
import io.chord.R
import io.chord.clients.ClientApi
import io.chord.clients.apis.ProjectsApi
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import io.chord.ui.extensions.addIcon
import io.chord.ui.fragments.project.ProjectListFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity()
{
	private val client: ProjectsApi = ClientApi.getProjectsApi()
	private val storage: SharedPreferencesAuthenticationStorage by inject()
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		// TODO use binding
		super.onCreate(savedInstanceState)
		this.setContentView(R.layout.activity_main)
		this.setSupportActionBar(this.findViewById(R.id.toolbar))
		
		val addProjectButton = this.findViewById<FloatingActionButton>(R.id.addProjectButton)
		addProjectButton.addIcon(this)
		addProjectButton.setOnClickListener { this.showCreateProjectDialog() }
	}
	
	override fun onCreateOptionsMenu(menu: Menu?): Boolean
	{
		this.menuInflater.inflate(R.menu.activity_main, menu)
		
		menu?.findItem(R.id.profile)
			?.icon =
			IconicsDrawable(this)
				.icon(FontAwesome.Icon.faw_user_circle1)
				.colorRes(R.color.backgroundPrimary)
				.actionBar()
		
		return true
	}
	
	private fun showCreateProjectDialog()
	{
		val fragment = this.supportFragmentManager.findFragmentById(R.id.projectListFragment) as ProjectListFragment
		fragment.create()
	}
}
