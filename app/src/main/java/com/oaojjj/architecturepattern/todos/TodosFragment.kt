package com.oaojjj.architecturepattern.todos

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.adapter.TodoAdapter
import com.oaojjj.architecturepattern.model.TodoModel

import com.oaojjj.architecturepattern.helper.SwipeHelper
import com.oaojjj.architecturepattern.customview.UnderlayButton
import com.oaojjj.architecturepattern.databinding.FragmentTodosBinding
import com.oaojjj.architecturepattern.listener.OnTodoCheckBoxClickListener
import com.oaojjj.architecturepattern.listener.OnUnderlayButtonClickListener
import com.oaojjj.architecturepattern.model.Todo


class TodosFragment : Fragment(), TodosContract.View, OnTodoCheckBoxClickListener {
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

    override fun onTodoCheckBoxClick(position: Int, checked: Boolean) {
        onUpdateCheckedTodo(position, checked)
    }

    override fun updateTodosView(item: MutableList<Todo>, position: Int) {
        TODO("Not yet implemented")
    }
}