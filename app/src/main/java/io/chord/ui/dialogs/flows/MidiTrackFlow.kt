package io.chord.ui.dialogs.flows

import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.clients.doOnSuccess
import io.chord.clients.models.MidiTrack
import io.chord.clients.observe
import io.chord.databinding.TrackMidiDialogFormBinding
import io.chord.services.managers.ProjectManager
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.customs.ConfirmationCudcOperationDialog
import io.chord.ui.dialogs.customs.FormCudcOperationDialog
import io.chord.ui.extensions.toBanerApiThrowable
import io.chord.ui.models.tracks.MidiTrackViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MidiTrackFlow(
	private val context: FragmentActivity
) : CudcFlow<MidiTrack>
{
	private val manager: ProjectManager.Companion = ProjectManager.Companion
	
	// TODO: generify
	
	private fun createDialog(operation: CudcOperation): FormCudcOperationDialog<TrackMidiDialogFormBinding>
	{
		return FormCudcOperationDialog(
			this.context,
			operation,
			this.context.getString(R.string.track_list_entity_name),
			R.layout.track_midi_dialog_form
		)
	}
	
	private fun createViewModel(model: MidiTrack): MidiTrackViewModel
	{
		val viewModel = MidiTrackViewModel()
		viewModel.fromModel(model)
		return viewModel
	}
	
	private fun save(
		dialog: FormCudcOperationDialog<TrackMidiDialogFormBinding>,
		binding: TrackMidiDialogFormBinding,
		observable: PublishSubject<MidiTrack>,
		track: MidiTrack
	)
	{
		this.manager.update()
			.doOnSubscribe {
				dialog.fragment.banner.dismiss()
				binding.nameLayout.isErrorEnabled = false
			}
			.doOnSuccess {
				observable.onNext(track)
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
	
	override fun create(): Observable<MidiTrack>
	{
		val observable = PublishSubject.create<MidiTrack>()
		val dialog = this.createDialog(CudcOperation.CREATE)
		
		dialog.onBind = { binding ->
			binding.track = this.createViewModel(MidiTrack("", 0, listOf(), listOf(), 1))
		}
		
		dialog.onValidate = { binding ->
			val trackToAdd = binding.track!!.toModel() as MidiTrack
			this.manager.tracks.add(trackToAdd)
			this.save(
				dialog,
				binding,
				observable,
				trackToAdd
			)
		}
		
		dialog.show()
		
		return observable
	}
	
	override fun update(model: MidiTrack): Observable<MidiTrack>
	{
		val observable = PublishSubject.create<MidiTrack>()
		val dialog = this.createDialog(CudcOperation.UPDATE)
		val indexToUpdate = this.manager.tracks.indexOf(model)
		
		dialog.onBind = { binding ->
			binding.track = this.createViewModel(model)
		}
		
		dialog.onValidate = { binding ->
			val trackToUpdate = binding.track!!.toModel() as MidiTrack
			this.manager.tracks.update(indexToUpdate, trackToUpdate)
			this.save(
				dialog,
				binding,
				observable,
				trackToUpdate
			)
		}
		
		dialog.show()
		
		return observable
	}
	
	override fun delete(model: MidiTrack): Observable<MidiTrack>
	{
		val observable = PublishSubject.create<MidiTrack>()
		val dialog = ConfirmationCudcOperationDialog(
			this.context,
			CudcOperation.DELETE,
			R.string.track_list_entity_name
		)
		
		dialog.onValidate = {
			this.manager.tracks.delete(model)
			this.manager.update()
				.doOnSuccess {
					observable.onNext(model)
				}
				.observe()
		}
		
		dialog.show()
		
		return observable
	}
	
	override fun clone(model: MidiTrack): Observable<MidiTrack>
	{
		val observable = PublishSubject.create<MidiTrack>()
		val dialog = this.createDialog(CudcOperation.CLONE)
		
		dialog.onBind = { binding ->
			binding.track = this.createViewModel(model.copy())
		}
		
		dialog.onValidate = { binding ->
			val trackToAdd = binding.track!!.toModel() as MidiTrack
			this.manager.tracks.add(trackToAdd)
			this.save(
				dialog,
				binding,
				observable,
				trackToAdd
			)
		}
		
		dialog.show()
		
		return observable
	}
}