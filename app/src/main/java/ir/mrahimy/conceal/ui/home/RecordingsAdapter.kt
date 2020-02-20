package ir.mrahimy.conceal.ui.home

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.base.BaseAdapter
import ir.mrahimy.conceal.data.Recording
import kotlinx.android.synthetic.main.item_recording.view.*

class RecordingsAdapter : BaseAdapter<Recording>(DIFF_CALLBACK) {

    var onStop: ((item: Recording, v: View) -> Unit)? = null
    var onPlay: ((item: Recording, v: View) -> Unit)? = null
    var onDelete: ((item: Recording, v: View) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recording>() {
            override fun areContentsTheSame(oldItem: Recording, newItem: Recording): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Recording, newItem: Recording): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_recording
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.stop?.setOnClickListener { v ->
            onStop?.invoke(getItem(holder.adapterPosition), v)
        }
        holder.itemView.play?.setOnClickListener { v ->
            onPlay?.invoke(getItem(holder.adapterPosition), v)
        }
        holder.itemView.delete?.setOnClickListener { v ->
            onDelete?.invoke(getItem(holder.adapterPosition), v)
        }
    }
}