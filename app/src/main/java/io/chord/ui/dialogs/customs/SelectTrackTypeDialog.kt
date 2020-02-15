package io.chord.ui.dialogs.customs

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.clients.models.MidiTrack
import io.chord.ui.dialogs.DialogParameters
import io.chord.ui.dialogs.sections.SectionDialogFragment
import io.chord.ui.dialogs.sections.SectionDialogItem
import io.chord.ui.dialogs.sections.SectionDialogItemViewHolder
import io.chord.ui.sections.Section
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@UseExperimental(ExperimentalStdlibApi::class)
class SelectTrackTypeDialog(
	override val context: FragmentActivity
): CustomDialog<(() -> Unit)>
{
	override val fragment: DialogFragment
	override var onValidate: (() -> Unit) = {}
	
	lateinit var onMidiSelected: (() -> Unit)
	
	init
	{
		this.fragment = SectionDialogFragment(
			DialogParameters(
				this.context,
				R.string.track_type_dialog_title,
				null,
				R.string.track_type_dialog_positive_button,
				R.string.track_type_dialog_negative_button
			)
		)
		
		val types = this.getTrackTypes()
		
		val section = Section(
			R.layout.section_dialog_item,
			{view -> SectionDialogItemViewHolder(view)},
			fragment
		)
		
		this.fragment.onValidate = {
			this.onValidate()
			when(types[it.index])
			{
				typeOf<MidiTrack>() -> this.onMidiSelected()
			}
		}
		
		this.fragment.addSection(section)
		
		types.forEach { type ->
			section.add(
				SectionDialogItem(
					0,
					null,
					this.getNameFromType(type)
				)
			)
		}
	}
	
	private fun getTrackTypes(): List<KType>
	{
		return listOf(
			typeOf<MidiTrack>()
		)
	}
	
	private fun getNameFromType(type: KType): String
	{
		return when(type)
		{
			typeOf<MidiTrack>() -> this.context.resources.getString(R.string.track_type_dialog_midi_track)
			else -> throw NoWhenBranchMatchedException("type is not a track type")
		}
	}
	
	override fun show()
	{
		this.fragment.show(
			this.context.supportFragmentManager,
			"fragment_select_track_type_dialog"
		)
	}
}