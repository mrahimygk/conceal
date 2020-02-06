package ir.mrahimy.conceal.ui.sample

import androidx.recyclerview.widget.DiffUtil
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.base.BaseAdapter
import ir.mrahimy.conceal.data.Sample

class SampleAdapter : BaseAdapter<Sample>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Sample>() {
            override fun areContentsTheSame(oldItem: Sample, newItem: Sample): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Sample, newItem: Sample): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_sample
    }
}