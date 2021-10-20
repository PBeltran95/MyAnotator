package ar.com.myanotator.myanotator.ui.fragments.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ar.com.myanotator.myanotator.R
import ar.com.myanotator.myanotator.data.models.Priority
import ar.com.myanotator.myanotator.data.models.ToDoData
import ar.com.myanotator.myanotator.databinding.RowLayoutBinding
import ar.com.myanotator.myanotator.ui.fragments.list.ListFragmentDirections

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {


    var dataList = emptyList<ToDoData>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val note = dataList[position]
        val priority = note.priority
        with(holder) {
            with(binding) {
                titleTv.text = note.title
                descriptionTv.text = note.description
                rowBackground.setOnClickListener {
                    val action = ListFragmentDirections.actionListFragmentToUpdateFragment(note)
                    holder.itemView.findNavController().navigate(action)
                }
                when (priority) {
                    Priority.LOW -> {
                        priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
                            itemView.context, R.color.green))
                    }
                    Priority.MEDIUM -> {priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
                        itemView.context, R.color.yellow))
                    }
                    Priority.HIGH -> {priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
                        itemView.context, R.color.red))
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(toDoData: List<ToDoData>){
        val toDoDiffUtil = DiffUtils(dataList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = dataList.size


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowLayoutBinding = RowLayoutBinding.bind(itemView)

    }
}