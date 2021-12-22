package com.oaojjj.architecturepattern.todos

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.model.TodoModel

import com.oaojjj.architecturepattern.utils.SwipeHelper
import com.oaojjj.architecturepattern.databinding.FragmentTodosBinding
import com.oaojjj.architecturepattern.todos.UnderlayButton.OnUnderlayButtonClickListener
import com.oaojjj.architecturepattern.model.Todo


class TodosFragment : Fragment(), TodosContract.View {
    private lateinit var binding: FragmentTodosBinding
    override lateinit var presenter: TodosContract.Presenter

    // itemTouchHelper
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var mAdapter: TodoAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("lifecycle_TodosFragment", "onCreate: ")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("lifecycle_TodosFragment", "onCreateView: ")
        binding = FragmentTodosBinding.inflate(inflater, container, false)

        // set up the toolbar
        val supportActionbar = (requireActivity() as AppCompatActivity).supportActionBar
        setToolbar(supportActionbar, getString(R.string.todo_list), false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("lifecycle_TodosFragment", "onViewCreated: ")

        // init data, adapter
        mAdapter = TodoAdapter(TodoModel.getDataList(), object : TodoItemListener {
            override fun onTodoCheckBoxClick(position: Int, checked: Boolean) {
                onUpdateCheckedTodo(position, checked)
            }
        }).apply {
        }

        binding.rvTodo.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
            it.addItemDecoration(DividerItemDecoration(it.context, 1))
        }

        // attach itemTouchHelper to recyclerview
        itemTouchHelper = ItemTouchHelper(object : SwipeHelper(requireContext(), binding.rvTodo) {
            override fun instantiateUnderlayButton(
                vh: RecyclerView.ViewHolder,
                buttons: MutableList<UnderlayButton>
            ) {
                buttons.add(
                    UnderlayButton(
                        text = "삭제",
                        background = ContextCompat.getColor(
                            requireContext(),
                            R.color.colorDelete
                        ),
                        listener = object : OnUnderlayButtonClickListener {
                            override fun onUnderlayButtonClick(pos: Int) {
                                onRemoveTodo(pos)
                            }
                        }
                    )
                )
                buttons.add(
                    UnderlayButton(
                        text = "수정",
                        background = ContextCompat.getColor(
                            requireContext(),
                            R.color.colorEdit
                        ),
                        listener = object : OnUnderlayButtonClickListener {
                            override fun onUnderlayButtonClick(pos: Int) {
                                showUpdateTodoDialog(pos)
                            }
                        }
                    )
                )
            }

        })
        itemTouchHelper.attachToRecyclerView(binding.rvTodo)
    }

    override fun onStart() {
        Log.d("lifecycle_TodosFragment", "onStart: ")
        super.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("lifecycle_TodosFragment", "onSaveInstanceState: ")
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        Log.d("lifecycle_TodosFragment", "onResume: ")
        super.onResume()
    }

    override fun onPause() {
        Log.d("lifecycle_TodosFragment", "onPause: ")
        super.onPause()
    }

    override fun onStop() {
        Log.d("lifecycle_TodosFragment", "onStop: ")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("lifecycle_TodosFragment", "onDestroyView: ")
        super.onDestroyView()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d("lifecycle_TodosFragment", "onViewStateRestored: ")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroy() {
        Log.d("lifecycle_TodosFragment", "onDestroy: ")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("lifecycle_TodosFragment", "onDetach: ")
        super.onDetach()
    }

    // View
    private fun showUpdateTodoDialog(pos: Int) {
        /* UpdateTodoDialog(
             object : OnUpdateTodoListener {
                 override fun onUpdateFinished() {
                     mAdapter.notifyItemChanged(pos)
                 }
             }, pos
         ).show(parentFragmentManager, null)*/
    }

    /**
     * 사용자 이벤트 발생 하고 호출되는 메소드(callback)
     * add, remove, update ...등
     * controller -> Model 에 요청
     * Model 에서 데이터를 조작 후 View 는 UI만 갱신(observable)
     */

    // 데이터 삭제
    private fun onRemoveTodo(pos: Int) {
        Thread {
            TodoModel.removeTodo(pos)
            requireActivity().runOnUiThread {
                mAdapter.notifyItemRemoved(pos)
                mAdapter.notifyItemRangeChanged(pos, TodoModel.size())
            }
        }.start()
        // (requireActivity() as MainActivity).showBottomAppBar(true)
    }

    // 데이터 수정(체크 유무)
    private fun onUpdateCheckedTodo(position: Int, checked: Boolean) {
        Thread {
            TodoModel.updateChecked(position, checked)
            requireActivity().runOnUiThread { mAdapter.notifyItemChanged(position) }
        }.start()
    }


    override fun updateTodosView(item: MutableList<Todo>, position: Int) {
        TODO("Not yet implemented")
    }

    private inner class TodoAdapter(
        todos: List<Todo>,
        private val itemListener: TodoItemListener
    ) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

        var todos: List<Todo> = todos
            @SuppressLint("NotifyDataSetChanged")
            set(todos) {
                field = todos
                notifyDataSetChanged()
            }

        inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cbTodo: CheckBox = itemView.findViewById(R.id.cb_todo)
            private val tvContents: TextView = itemView.findViewById(R.id.tv_todo_contents)

            fun bind(item: Todo) {
                cbTodo.isChecked = item.checked
                tvContents.text = item.content

                // 취소선
                if (cbTodo.isChecked) tvContents.paintFlags =
                    tvContents.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else tvContents.paintFlags = 0

                cbTodo.setOnClickListener {
                    itemListener.onTodoCheckBoxClick(adapterPosition, cbTodo.isChecked)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
            return TodoViewHolder(view)
        }

        override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
            holder.bind(todos[position])
        }

        override fun getItemCount(): Int = todos.size

    }

    interface TodoItemListener {
        fun onTodoCheckBoxClick(position: Int, checked: Boolean)
    }
}