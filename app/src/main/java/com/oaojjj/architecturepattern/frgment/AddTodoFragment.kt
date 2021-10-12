package com.oaojjj.architecturepattern.frgment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.oaojjj.architecturepattern.MainActivity
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.databinding.FragmentAddTodoBinding
import com.oaojjj.architecturepattern.listener.OnFinishedAddTodoListener
import com.oaojjj.architecturepattern.model.TodoModel
import com.oaojjj.architecturepattern.utils.Util


class AddTodoFragment : Fragment(), OnFinishedAddTodoListener {
    private lateinit var binding: FragmentAddTodoBinding
    private lateinit var prevTitle: String

    override fun onAttach(context: Context) {
        MainActivity.expendedAppBarLayout()
        prevTitle = (requireActivity() as AppCompatActivity).supportActionBar?.title.toString()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "할 일 추가"
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
        Handler(Looper.getMainLooper()).postDelayed({
            Util.showInput(requireContext(), binding.etTodoContents)
        }, 100)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onFinishedAddTodo() {
        Log.d("AddTodoFragment_TAG", "onFinishedAddTodo: ${binding.etTodoContents.text}")
        Util.hideInput(binding.etTodoContents)
        Thread { TodoModel.addTodo(binding.etTodoContents.text.toString()) }.start()
    }

    override fun onDestroy() {
        Util.hideInput(binding.etTodoContents)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = prevTitle
        super.onDestroy()
    }
}