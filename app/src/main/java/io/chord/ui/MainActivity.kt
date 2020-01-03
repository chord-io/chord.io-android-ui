package io.chord.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import android.widget.Switch
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikepenz.iconics.*
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorRes
import com.warkiz.widget.IndicatorSeekBar
import io.chord.R
import io.chord.client.ClientApi
import io.chord.client.apis.ProjectsApi
import io.chord.client.models.ProjectDto
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import io.chord.ui.dialog.cudc.CudcFormOperationDialogFragment
import io.chord.ui.dialog.cudc.CudcOperation
import io.chord.ui.dialog.cudc.CudcOperationInformation
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity()
{
	private val authenticationStorage: SharedPreferencesAuthenticationStorage by inject()
	private val client: ProjectsApi = ClientApi.getProjectsApi()
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		this.setContentView(R.layout.activity_main)
		this.setSupportActionBar(this.findViewById(R.id.toolbar))
		
		val addProjectButton = this.findViewById<FloatingActionButton>(R.id.addProjectButton)
		addProjectButton.setImageDrawable(
			IconicsDrawable(this)
				.icon(FontAwesome.Icon.faw_plus)
				.colorRes(R.color.backgroundPrimary)
		)
		addProjectButton.setOnClickListener {
			run {
				this.showCreateProjectDialog()
			}
		}
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
		val fragmentManager = this.supportFragmentManager
		val dialogFragment =
			CudcFormOperationDialogFragment(
				CudcOperationInformation(
					CudcOperation.CREATE,
					this.getString(R.string.project_entity_name)
				),
				R.layout.project_dialog_form
			)
		
		dialogFragment.onLayoutUpdatedListener = {
			val name = it!!.findViewById<EditText>(R.id.name)
			val author = it.findViewById<EditText>(R.id.author)
			val tempo = it.findViewById<IndicatorSeekBar>(R.id.tempo)
			val isPrivate = it.findViewById<Switch>(R.id.isPrivate)
			val project = ProjectDto(
				name.text.toString(),
				author.text.toString(),
				tempo.progress,
				isPrivate.isChecked,
				listOf(),
				listOf()
			)
			
			this.client.create(project)
				.doOnSuccess {
					// TODO handle get real project object
				}
				.doOnError {
					// TODO handle error
				}
		}
		
		dialogFragment.show(fragmentManager, "fragment_project_create_form")
	}
}
