package com.oaojjj.architecturepattern.frgment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.adapter.TodoAdapter
import com.oaojjj.architecturepattern.databinding.FragmentActiveListBinding
import com.oaojjj.architecturepattern.model.TodoModel

import com.oaojjj.architecturepattern.controller.SwipeController
import com.oaojjj.architecturepattern.customview.UnderlayButton
import com.oaojjj.architecturepattern.listener.OnTodoCheckBoxClickListener


class ActiveListFragment : Fragment(), OnTodoCheckBoxClickListener {
    private lateinit var binding: FragmentActiveListBinding

    // itemTouchHelper
    private lateinit var itemTouchHelper: ItemTouchHelper

    private lateinit var mAdapter: TodoAdapter

    override fun onAttach(context: Context) {
        // model
        TodoModel.setContext(context)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ActiveListFragment_TAG", "onCreateView: ")
        binding = FragmentActiveListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ActiveListFragment_TAG", "onViewCreated: ")
        // init data, adapter
        mAdapter =
            TodoAdapter(requireContext(), TodoModel.getDataList())
                .apply { setOnTodoCheckBoxListener(this@ActiveListFragment) }
        binding.rvTodo.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
            it.addItemDecoration(DividerItemDecoration(it.context, 1))
        }

        // attach itemTouchHelper to recyclerview
        itemTouchHelper =
            ItemTouchHelper(object : SwipeController(requireContext(), binding.rvTodo) {
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
                        )
                    )
                    buttons.add(
                        UnderlayButton(
                            text = "수정",
                            background = ContextCompat.getColor(
                                requireContext(),
                                R.color.colorEdit
                            ),
                        )
                    )
                }

            })
        itemTouchHelper.attachToRecyclerView(binding.rvTodo)

    }

    override fun onStart() {
        Log.d("ActiveListFragment_TAG", "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.d("ActiveListFragment_TAG", "onResume: ")
        requireActivity().title = getString(R.string.todo_list)
        super.onResume()
    }

    override fun onStop() {
        Log.d("ActiveListFragment_TAG", "onStop: ")
        super.onStop()
    }

    override fun onPause() {
        Log.d("ActiveListFragment_TAG", "onPause: ")
        super.onPause()
    }

    override fun onDestroyView() {
        Log.d("ActiveListFragment_TAG", "onDestroyView: ")
        super.onDestroyView()
    }

    /**
     * 사용자 이벤트 발생 하고 호출되는 메소드(callback)
     * add, remove, update ...등
     * controller -> Model 에 데이터 추가 요청
     * Model 에서 데이터를 조작 후 View 는 UI만 갱신
     */

//    // 데이터 추가
//    private fun onAddTodo(contents: String?) {
//        if (contents != null) {
//            TodoModel.addTodo(contents)
//            mAdapter.notifyItemInserted(TodoModel.size())
//        }
//    }

    // 데이터 수정
    private fun onUpdateTodo(contents: String) {
        TodoModel.updateTodo(contents)
        mAdapter.notifyItemChanged(TodoModel.getPosition())
    }

    // 데이터 삭제
    private fun onRemoveTodo() {
        TodoModel.removeTodo()
        mAdapter.notifyItemRemoved(TodoModel.getPosition())
        mAdapter.notifyItemRangeChanged(TodoModel.getPosition(), TodoModel.size())
    }

    // 데이터 수정(체크 유무)
    private fun onUpdateCheckedTodo(position: Int, checked: Boolean) {
        TodoModel.updateChecked(position, checked)
        mAdapter.notifyItemChanged(position)
    }

    override fun onTodoCheckBoxClick(position: Int, checked: Boolean) {
        onUpdateCheckedTodo(position, checked)
    }
}