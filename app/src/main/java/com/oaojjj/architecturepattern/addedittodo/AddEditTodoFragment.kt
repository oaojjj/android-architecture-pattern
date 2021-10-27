package com.oaojjj.architecturepattern.addedittodo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.databinding.FragmentAddEditTodoBinding
import com.oaojjj.architecturepattern.listener.OnFinishedAddTodoListener
import com.oaojjj.architecturepattern.model.TodoModel
import com.oaojjj.architecturepattern.utils.Util

class AddEditTodoFragment : Fragment(), AddEditTodoContract.View, OnFinishedAddTodoListener {
    private lateinit var binding: FragmentAddEditTodoBinding
    override lateinit var presenter: AddEditTodoContract.Presenter

    override fun onAttach(context: Context) {
        setHasOptionsMenu(true)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AddTodoFragment", "onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditTodoBinding.inflate(inflater, container, false)

        // set up the toolbar
        val supportActionbar = (requireActivity() as AppCompatActivity).supportActionBar
        setToolbar(supportActionbar, getString(R.string.todo_add), true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Handler(Looper.getMainLooper()).postDelayed({
            Util.showInput(requireContext(), binding.etTodoContents)
        }, 300)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_todo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                Log.d("TodosFragment", "onOptionsItemSelected: home")
            }
            R.id.save_todo -> {
                // TODO: 2021-10-27
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        Util.hideInput(requireActivity().currentFocus)
        super.onDestroyView()
    }

    // Controller: MainActivity 에서 넘어온 이벤트 -> Model 데이터 추가 요청
    override fun onFinishedAddTodo() {
        Log.d("AddTodoFragment_TAG", "onFinishedAddTodo: ${binding.etTodoContents.text}")
        Thread { TodoModel.addTodo(binding.etTodoContents.text.toString()) }.start()
    }
}