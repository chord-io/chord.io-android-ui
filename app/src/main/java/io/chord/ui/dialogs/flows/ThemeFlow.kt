package io.chord.ui.dialogs.flows

import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.clients.doOnSuccess
import io.chord.clients.models.Theme
import io.chord.clients.observe
import io.chord.databinding.ThemeDialogFormBinding
import io.chord.services.managers.ProjectManager
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.customs.ConfirmationCudcOperationDialog
import io.chord.ui.dialogs.customs.FormCudcOperationDialog
import io.chord.ui.extensions.toBanerApiThrowable
import io.chord.ui.models.ThemeViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ThemeFlow(
	private val context: FragmentActivity
) : CudcFlow<Theme>
{
	private val manager: ProjectManager.Companion = ProjectManager.Companion
	
	private fun createDialog(operation: CudcOperation): FormCudcOperationDialog<ThemeDialogFormBinding>
	{
		return FormCudcOperationDialog(
			this.context,
			operation,
			this.context.getString(R.string.theme_entity_name),
			R.layout.theme_dialog_form
		)
	}
	
	private fun createViewModel(model: Theme): ThemeViewModel
	{
		val viewModel = ThemeViewModel()
		viewModel.fromModel(model)
		return viewModel
	}
	
	private fun save(
		dialog: FormCudcOperationDialog<ThemeDialogFormBinding>,
		binding: ThemeDialogFormBinding,
		observable: PublishSubject<Theme>,
		theme: Theme
	)
	{
		this.manager.update()
			.doOnSubscribe {
				dialog.fragment.banner.dismiss()
				binding.nameLayout.isErrorEnabled = false
			}
			.doOnSuccess {
				observable.onNext(theme)
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
							.observe()
					}
					.doOnPostObservation {
						dialog.fragment.invalidate()
					}
					.observe()
			}
			.observe()
	}
	
	override fun create(): Observable<Theme>
	{
		val observable = PublishSubject.create<Theme>()
		val dialog = this.createDialog(CudcOperation.CREATE)
		
		dialog.onBind = { binding ->
			binding.theme = this.createViewModel(Theme("", listOf()))
		}
		
		dialog.onValidate = { binding ->
			val themeToAdd = binding.theme!!.toModel()
			this.manager.themes.add(themeToAdd)
			this.save(
				dialog,
				binding,
				observable,
				themeToAdd
			)
		}
		
		dialog.show()
		
		return observable
	}
	
	override fun update(model: Theme): Observable<Theme>
	{
		val observable = PublishSubject.create<Theme>()
		val dialog = this.createDialog(CudcOperation.UPDATE)
		val indexToUpdate = this.manager.themes.indexOf(model)
		
		dialog.onBind = { binding ->
			binding.theme = this.createViewModel(model)
		}
		
		dialog.onValidate = { binding ->
			val themeToUpdate = binding.theme!!.toModel()
			this.manager.themes.update(indexToUpdate, themeToUpdate)
			this.save(
				dialog,
				binding,
				observable,
				themeToUpdate
			)
		}
		
		dialog.show()
		
		return observable
	}
	
	override fun delete(model: Theme): Observable<Theme>
	{
		val observable = PublishSubject.create<Theme>()
		val dialog = ConfirmationCudcOperationDialog(
			this.context,
			CudcOperation.DELETE,
			R.string.theme_entity_name
		)
		
		dialog.onValidate = {
			this.manager.themes.delete(model)
			this.manager.update()
				.doOnSuccess {
					observable.onNext(model)
				}
				.observe()
		}
		
		dialog.show()
		
		return observable
	}
	
	override fun clone(model: Theme): Observable<Theme>
	{
		val observable = PublishSubject.create<Theme>()
		val dialog = this.createDialog(CudcOperation.CLONE)
		
		dialog.onBind = { binding ->
			binding.theme = this.createViewModel(model.copy())
		}
		
		dialog.onValidate = { binding ->
			val themeToAdd = binding.theme!!.toModel()
			this.manager.themes.add(themeToAdd)
			this.save(
				dialog,
				binding,
				observable,
				themeToAdd
			)
		}
		
		dialog.show()
		
		return observable
	}
}