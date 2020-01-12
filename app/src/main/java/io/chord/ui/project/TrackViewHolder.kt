//package io.chord.ui.project
//
//import android.view.View
//import android.widget.ProgressBar
//import android.widget.TextView
//import androidx.databinding.DataBindingUtil
//import com.mikepenz.iconics.IconicsDrawable
//import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
//import com.mikepenz.iconics.utils.colorRes
//import com.mikepenz.iconics.view.IconicsImageView
//import io.chord.R
//import io.chord.client.models.Project
//import io.chord.client.models.Track
//import io.chord.databinding.ProjectListItemBinding
//import io.chord.databinding.TrackListItemBinding
//import io.chord.ui.ChordIOApplication
//import io.chord.ui.section.ClickListener
//import io.chord.ui.section.ViewHolderBase
//
//class TrackViewHolder(
//    view: View
//) : ViewHolderBase<Track, TrackViewHolder>(view)
//{
//    val binding = DataBindingUtil.bind<TrackListItemBinding>(this.itemView)!!
//
//    override fun bind(item: Track, clickListener: ClickListener<Track, TrackViewHolder>)
//    {
//        val icon = if(item.channel == 10) FontAwesome.Icon.faw_drum else FontAwesome.Icon.faw_guitar
//
//        binding.apply {
//            this.icon.icon = IconicsDrawable(itemView.context)
//                .icon(icon)
//                .colorRes(R.color.colorAccent)
//
//            this.name.text = item.name
//
//            this.information.text = ChordIOApplication.instance.resources.getString(
//                R.string.track_list_item_information,
//                item.channel.toString()
//            )
//
//            this.loader.visibility = View.GONE
//        }
//
//        this.itemView.setOnClickListener {
//            clickListener.onItemClicked(item, this)
//        }
//
//        this.itemView.setOnLongClickListener {
//            clickListener.onItemLongClicked(item, this)
//        }
//    }
//}