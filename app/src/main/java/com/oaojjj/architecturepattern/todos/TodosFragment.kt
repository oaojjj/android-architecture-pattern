package com.oaojjj.architecturepattern.frgment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oaojjj.architecturepattern.main.MainActivity
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.adapter.TodoAdapter
import com.oaojjj.architecturepattern.model.TodoModel

import com.oaojjj.architecturepattern.controller.SwipeHelper
import com.oaojjj.architecturepattern.customview.UnderlayButton
import com.oaojjj.architecturepattern.databinding.FragmentTodosBinding
import com.oaojjj.architecturepattern.listener.OnTodoCheckBoxClickListener
import com.oaojjj.architecturepattern.listener.OnUnderlayButtonClickListener
import com.oaojjj.architecturepattern.listener.OnUpdateTodoListener
import com.oaojjj.architecturepattern.model.Todo
import com.oaojjj.architecturepattern.todos.TodosContract


class TodosFragment : Fragment(), TodosContract.View, OnTodoCheckBoxClickListener {
    private lateinit var binding: FragmentTodosBinding
    private lateinit var presenter: TodosContract.Presenter

    // itemTouchHelper
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var mAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ActiveListFragment_TAG", "onCreateView: ")
        binding = FragmentTodosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ActiveListFragment_TAG", "onViewCreated: ")
        // init data, adapter

        mAdapter = TodoAdapter(requireContext(), TodoModel.getDataList()).apply {
            setOnTodoCheckBoxListener(this@TodosFragment)
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_todo, menu)
    }


    // View
    private fun showUpdateTodoDialog(pos: Int) {
        UpdateTodoDialog(
            object : OnUpdateTodoListener {
                override fun onUpdateFinished() {
                    mAdapter.notifyItemChanged(pos)
                }
            }, pos
        ).show(parentFragmentManager, null)
    }

    override fun onResume() {
        requireActivity().title = getString(R.string.todo_list)
        super.onResume()
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

    override fun onTodoCheckBoxClick(position: Int, checked: Boolean) {
        onUpdateCheckedTodo(position, checked)
    }

    override fun updateTodosView(item: MutableList<Todo>, position: Int) {
        TODO("Not yet implemented")
    }
}