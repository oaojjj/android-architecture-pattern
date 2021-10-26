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
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.databinding.FragmentAddTodoBinding
import com.oaojjj.architecturepattern.listener.OnFinishedAddTodoListener
import com.oaojjj.architecturepattern.model.TodoModel
import com.oaojjj.architecturepattern.utils.Util


class AddTodoFragment : Fragment(), OnFinishedAddTodoListener {
    private lateinit var binding: FragmentAddTodoBinding
    private lateinit var prevTitle: String
    private var supportActionBar: ActionBar? = null

    override fun onAttach(context: Context) {
        initToolbar()
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
        }, 300)
        super.onViewCreated(view, savedInstanceState)
    }

    // View
    private fun initToolbar() {
        supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar.apply {
            prevTitle = this?.title.toString()
            this?.title = getString(R.string.todo_add)
            this?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onDestroyView() {
        Util.hideInput(requireActivity().currentFocus)
        supportActionBar?.title = prevTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onDestroyView()
    }

    // Controller: MainActivity 에서 넘어온 이벤트 -> Model 데이터 추가 요청
    override fun onFinishedAddTodo() {
        Log.d("AddTodoFragment_TAG", "onFinishedAddTodo: ${binding.etTodoContents.text}")
        Thread { TodoModel.addTodo(binding.etTodoContents.text.toString()) }.start()
    }
}