package com.oaojjj.architecturepattern.frgment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.databinding.FragmentAddTodoBinding
import com.oaojjj.architecturepattern.listener.OnFinishedAddTodoListener
import com.oaojjj.architecturepattern.model.TodoModel


class AddTodoFragment : Fragment(), OnFinishedAddTodoListener {
    private lateinit var binding: FragmentAddTodoBinding

    override fun onAttach(context: Context) {
        requireActivity().title = getString(R.string.todo_add)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //fragment 키보드 올리기
        showInput()
    }

    private fun showInput() {
        binding.etTodoContents.requestFocus()

        val mInputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.toggleSoftInputFromWindow(
            binding.etTodoContents.applicationWindowToken,
            InputMethodManager.SHOW_FORCED, 0
        )
    }

    override fun onFinishedAddTodo() {
        Log.d("AddTodoFragment_TAG", "onFinishedAddTodo: ${binding.etTodoContents.text}")
        TodoModel.addTodo(binding.etTodoContents.text.toString())
    }
}