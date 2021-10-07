package com.oaojjj.architecturepattern.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.listener.OnTodoCheckBoxClickListener
import com.oaojjj.architecturepattern.model.Todo

class TodoAdapter(private val mContext: Context, private val todoList: MutableList<Todo>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    // listener
    private lateinit var mTodoCheckBoxListener: OnTodoCheckBoxClickListener
    fun setOnTodoCheckBoxListener(listener: OnTodoCheckBoxClickListener) {
        mTodoCheckBoxListener = listener
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cbTodo: CheckBox = itemView.findViewById(R.id.cb_todo)
        private val tvContents: TextView = itemView.findViewById(R.id.tv_todo_contents)
        fun bind(item: Todo) {
            cbTodo.isChecked = item.checked
            tvContents.text = item.content

            cbTodo.setOnClickListener {
                mTodoCheckBoxListener.onTodoCheckBoxClick(adapterPosition, cbTodo.isChecked)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    override fun getItemCount(): Int = todoList.size


}