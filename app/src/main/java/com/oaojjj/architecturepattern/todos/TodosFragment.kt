package com.oaojjj.architecturepattern.todos

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oaojjj.architecturepattern.R

import com.oaojjj.architecturepattern.databinding.FragmentTodosBinding
import com.oaojjj.architecturepattern.data.Todo
import com.oaojjj.architecturepattern.databinding.ItemTodoBinding
import com.oaojjj.architecturepattern.main.MainPresenter


class TodosFragment(private val mainPresenter: MainPresenter) : Fragment(), TodosContract.View {
    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!

    override lateinit var presenter: TodosContract.Presenter

    /**
     * Listener for clicks on todos in the RecyclerView.
     */
    private var itemListener: TodoItemListener = object : TodoItemListener {

        override fun onTodoClick(clickedTodo: Todo) {
            presenter.openTodoDetails(clickedTodo)
        }

        override fun onCompleteTodoClick(completedTodo: Todo) {
            presenter.completeTodo(completedTodo)
        }

        override fun onActivateTodoClick(activatedTodo: Todo) {
            presenter.activateTodo(activatedTodo)
        }

    }

    private var listAdapter: TodoAdapter = TodoAdapter(ArrayList(0), itemListener)

    override fun onAttach(context: Context) {
        Log.d("lifecycle_TodosFragment", "onAttach: ")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("lifecycle_TodosFragment", "onCreate: ")
        super.onCreate(savedInstanceState)
        presenter = mainPresenter.todosPresenter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("lifecycle_TodosFragment", "onCreateView: ")
        _binding = FragmentTodosBinding.inflate(inflater, container, false)

        // set up the toolbar
        val supportActionbar = (requireActivity() as AppCompatActivity).supportActionBar
        setToolbar(supportActionbar, getString(R.string.todo_list), false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("lifecycle_TodosFragment", "onViewCreated: ")

        with(binding.rvTodo) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), 1))
        }

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
        _binding = null
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

    override fun showTodoDetailsUi(id: Long?) {

    }

    private class TodoAdapter(
        todos: List<Todo>,
        private val itemListener: TodoItemListener
    ) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

        var todos: List<Todo> = todos
            @SuppressLint("NotifyDataSetChanged")
            set(todos) {
                field = todos
                notifyDataSetChanged()
            }

        inner class TodoViewHolder(val binding: ItemTodoBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(todo: Todo) {
                with(binding) {
                    cbTodo.isChecked = todo.isCompleted
                    tvTodoContents.text = todo.content

                    if (todo.isCompleted) {
                        itemListener.onCompleteTodoClick(todo)
                    } else {
                        itemListener.onActivateTodoClick(todo)
                    }
                }
                itemView.setOnClickListener { itemListener.onTodoClick(todo) }

                // 취소선
//                if (cbTodo.isChecked) tvContents.paintFlags =
//                    tvContents.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                else tvContents.paintFlags = 0
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {

            return TodoViewHolder(
                ItemTodoBinding.bind(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
                )
            )
        }

        override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
            holder.bind(todos[position])
        }

        override fun getItemCount(): Int = todos.size

    }

    interface TodoItemListener {

        fun onTodoClick(clickedTodo: Todo)

        fun onCompleteTodoClick(completedTodo: Todo)

        fun onActivateTodoClick(activatedTodo: Todo)
    }
}