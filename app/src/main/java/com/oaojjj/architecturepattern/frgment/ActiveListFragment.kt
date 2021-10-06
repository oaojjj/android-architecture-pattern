package com.oaojjj.architecturepattern.frgment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.adapter.TodoAdapter
import com.oaojjj.architecturepattern.databinding.FragmentActiveListBinding
import com.oaojjj.architecturepattern.listener.OnTodoClickListener
import com.oaojjj.architecturepattern.model.TodoModel

class ActiveListFragment : Fragment(), OnTodoClickListener {
    private lateinit var binding: FragmentActiveListBinding

    private lateinit var todoModel: TodoModel
    private lateinit var mAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActiveListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // model
        todoModel = TodoModel(requireContext())

        // init data, adapter
        mAdapter = TodoAdapter(requireContext(), todoModel.getDataList())
            .apply { setOnTodoClickListener(this@ActiveListFragment) }
        binding.rvTodo.adapter = mAdapter
        binding.rvTodo.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onTodoCheckClick(position: Int, checked: Boolean) {
        updateCheckedTodo(position, checked)
    }

    override fun onTodoLongClick(view: View?, position: Int) {
        todoModel.setPosition(position)
    }

    /**
     * 사용자 이벤트 발생 하고 호출되는 메소드(callback)
     * add, remove, update ...등
     * controller -> Model 에 데이터 추가 요청
     * Model 에서 데이터를 조작 후 View 는 UI만 갱신
     */

    // 데이터 추가
    private fun addTodo(contents: String?) {
        if (contents != null) {
            todoModel.addTodo(contents)
            mAdapter.notifyItemInserted(todoModel.size())
        }
    }

    // 데이터 수정
    private fun updateTodo(contents: String) {
        todoModel.updateTodo(contents)
        mAdapter.notifyItemChanged(todoModel.getPosition())
    }

    // 데이터 삭제
    private fun removeTodo() {
        todoModel.removeTodo()
        mAdapter.notifyItemRemoved(todoModel.getPosition())
    }


    // 데이터 수정(체크 유무)
    private fun updateCheckedTodo(position: Int, checked: Boolean) {
        todoModel.updateChecked(position, checked)
        mAdapter.notifyItemChanged(position)
    }
}