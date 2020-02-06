package ir.mrahimy.conceal.ba


import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.mrahimy.conceal.base.BaseAdapter

@Suppress("UNCHECKED_CAST")
@BindingAdapter("data")
fun <T> RecyclerView.setData(data: MutableList<T>?) {
    if (adapter is BaseAdapter<*>) {
        (adapter as BaseAdapter<T>).submitList(data)
    }
}