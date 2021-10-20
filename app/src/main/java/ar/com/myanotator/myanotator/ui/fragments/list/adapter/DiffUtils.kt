package ar.com.myanotator.myanotator.ui.fragments.list.adapter

import androidx.recyclerview.widget.DiffUtil
import ar.com.myanotator.myanotator.data.models.ToDoData

class DiffUtils(
    private val oldList : List<ToDoData>,
    private val newList : List<ToDoData>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].description == newList[newItemPosition].description
    }


}