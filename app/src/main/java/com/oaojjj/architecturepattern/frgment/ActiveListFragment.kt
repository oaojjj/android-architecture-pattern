package com.oaojjj.architecturepattern.frgment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.oaojjj.architecturepattern.adapter.TodoAdapter
import com.oaojjj.architecturepattern.databinding.FragmentActiveListBinding
import com.oaojjj.architecturepattern.model.TodoModel

import com.oaojjj.architecturepattern.controller.SwipeController
import com.oaojjj.architecturepattern.listener.OnTodoCheckBoxClickListener


class ActiveListFragment : Fragment(), OnTodoCheckBoxClickListener {
    private lateinit var binding: FragmentActiveListBinding

    // itemTouchHelper
    private lateinit var itemTouchHelper: ItemTouchHelper
    private val swipeController: SwipeController = SwipeController()

    private lateinit var todoModel: TodoModel
    private lateinit var mAdapter: TodoAdapter

    override fun onAttach(context: Context) {
        // model
        todoModel = TodoModel(context)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActiveListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init data, adapter
        mAdapter =
            TodoAdapter(requireContext(), todoModel.getDataList())
                .apply { setOnTodoCheckBoxListener(this@ActiveListFragment) }
        binding.rvTodo.adapter = mAdapter
        binding.rvTodo.layoutManager = LinearLayoutManager(requireContext())

        // attach itemTouchHelper to recyclerview
        itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(binding.rvTodo)

    }

    /**
     * 사용자 이벤트 발생 하고 호출되는 메소드(callback)
     * add, remove, update ...등
     * controller -> Model 에 데이터 추가 요청
     * Model 에서 데이터를 조작 후 View 는 UI만 갱신
     */

    // 데이터 추가
    fun onAddTodo(contents: String?) {
        if (contents != null) {
            todoModel.addTodo(contents)
            mAdapter.notifyItemInserted(todoModel.size())
        }
    }

    // 데이터 수정
    fun onUpdateTodo(contents: String) {
        todoModel.updateTodo(contents)
        mAdapter.notifyItemChanged(todoModel.getPosition())
    }

    // 데이터 삭제
    fun onRemoveTodo() {
        todoModel.removeTodo()
        mAdapter.notifyItemRemoved(todoModel.getPosition())
        mAdapter.notifyItemRangeChanged(todoModel.getPosition(), todoModel.size())
    }

    // 데이터 수정(체크 유무)
    private fun onUpdateCheckedTodo(position: Int, checked: Boolean) {
        todoModel.updateChecked(position, checked)
        mAdapter.notifyItemChanged(position)
    }

    override fun onTodoCheckBoxClick(position: Int, checked: Boolean) {
        onUpdateCheckedTodo(position, checked)
    }
}