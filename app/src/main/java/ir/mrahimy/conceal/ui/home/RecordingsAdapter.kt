package ir.mrahimy.conceal.ui.home

import androidx.recyclerview.widget.DiffUtil
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.base.BaseAdapter
import ir.mrahimy.conceal.data.Recording
import ir.mrahimy.conceal.data.Sample

class RecordingsAdapter : BaseAdapter<Recording>(DIFF_CALLBACK) {

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
}