package io.chord.ui.dialogs.flows

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.clients.doOnSuccess
import io.chord.clients.models.Project
import io.chord.clients.observe
import io.chord.databinding.ProjectDialogFormBinding
import io.chord.services.managers.ProjectManager
import io.chord.ui.ChordIOApplication
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.customs.ConfirmationCudcOperationDialog
import io.chord.ui.dialogs.customs.FormCudcOperationDialog
import io.chord.ui.extensions.toBanerApiThrowable
import io.chord.ui.models.ProjectViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class ProjectFlow(
	private val context: FragmentActivity
) : CudcFlow<Project>
{
	private val manager: ProjectManager.Companion = ProjectManager.Companion
	
	private fun createDialog(operation: CudcOperation): FormCudcOperationDialog<ProjectDialogFormBinding>
	{
		return FormCudcOperationDialog(
			this.context,
			operation,
			this.context.getString(R.string.project_entity_name),
			R.layout.project_dialog_form
		)
	}
	
	private fun createViewModel(model: Project): ProjectViewModel
	{
		val viewModel = ProjectViewModel()
		viewModel.fromModel(model)
		return viewModel
	}
	
	@SuppressLint("CheckResult")
	private fun observe(
		operation: Observable<Project>,
		observable: PublishSubject<Project>,
		dialog: FormCudcOperationDialog<ProjectDialogFormBinding>,
		binding: ProjectDialogFormBinding
	)
	{
		operation
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe {
				dialog.fragment.banner.dismiss()
				binding.nameLayout.isErrorEnabled = false
				binding.authorLayout.isErrorEnabled = false
				binding.author.isEnabled = false
			}
			.doOnSuccess {
				observable.onNext(it)
				dialog.fragment.validate()
			}
			.doOnError { throwable ->
				throwable
					.toBanerApiThrowable(dialog.fragment.banner)
					.doOnValidationError { mapper ->
						mapper
							.map("Name") { error ->
								binding.nameLayout.isErrorEnabled = true
								binding.nameLayout.error = error
							}
							.map("AuthorId") { error ->
								binding.authorLayout.isErrorEnabled = true
								binding.authorLayout.error = error
								binding.author.isEnabled = false
							}
							.observe()
					}
					.doOnPostObservation {
						dialog.fragment.invalidate()
						binding.author.isEnabled = false
					}
					.observe()
			}
			.observe()
	}
	
	override fun create(): Observable<Project>
	{
		val observable = PublishSubject.create<Project>()
		val dialog = this.createDialog(CudcOperation.CREATE)
		
		dialog.onBind = { binding ->
			binding.application = ChordIOApplication.instance
			binding.project = ProjectViewModel()
		}
		
		dialog.onValidate = { binding: ProjectDialogFormBinding ->
			val projectToCreate = binding.project!!.toModel()
			this.observe(
				this.manager.create(projectToCreate),
				observable,
				dialog,
				binding
			)
		}
		
		dialog.show()
		
		return observable
	}
	
	override fun update(model: Project): Observable<Project>
	{
		val observable = PublishSubject.create<Project>()
		val dialog = this.createDialog(CudcOperation.UPDATE)
		
		dialog.onBind = { binding ->
			binding.application = ChordIOApplication.instance
			binding.project = this.createViewModel(model)
		}
		
		dialog.onValidate = { binding: ProjectDialogFormBinding ->
			val projectToUpdate = binding.project!!.toModel()
			this.observe(
				this.manager.update(projectToUpdate),
				observable,
				dialog,
				binding
			)
		}
		
		dialog.show()
		
		return observable
	}
	
	override fun delete(model: Project): Observable<Project>
	{
		val observable = PublishSubject.create<Project>()
		val dialog = ConfirmationCudcOperationDialog(
			this.context,
			CudcOperation.DELETE,
			R.string.project_entity_name
		)
		
		dialog.onValidate = {
			this.manager.delete(model)
				.doOnSuccess {
					observable.onNext(it)
				}
				.observe()
		}
		
		dialog.show()
		
		return observable
	}
	
	override fun clone(model: Project): Observable<Project>
	{
		val observable = PublishSubject.create<Project>()
		val dialog = this.createDialog(CudcOperation.CLONE)
		
		dialog.onBind = { binding ->
			binding.application = ChordIOApplication.instance
			binding.project = this.createViewModel(model.copy())
		}
		
		dialog.onValidate = { binding: ProjectDialogFormBinding ->
			val projectToCreate = binding.project!!.toModel()
			this.observe(
				this.manager.create(projectToCreate),
				observable,
				dialog,
				binding
			)
		}
		
		dialog.show()
		
		return observable
	}
	
	fun getAllByAuthor(): Observable<List<Project>>
	{
		return this.manager.getAllByAuthor()
	}
}
