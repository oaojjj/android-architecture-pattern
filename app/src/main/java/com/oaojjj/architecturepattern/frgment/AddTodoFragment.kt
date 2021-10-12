package com.oaojjj.architecturepattern.frgment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.oaojjj.architecturepattern.MainActivity
import com.oaojjj.architecturepattern.databinding.FragmentAddTodoBinding
import com.oaojjj.architecturepattern.listener.OnFinishedAddTodoListener
import com.oaojjj.architecturepattern.model.TodoModel
import com.oaojjj.architecturepattern.utils.Util


class AddTodoFragment : Fragment(), OnFinishedAddTodoListener {
    private lateinit var binding: FragmentAddTodoBinding
    private lateinit var prevTitle: String
    private var supportActionBar: ActionBar? = null

    override fun onAttach(context: Context) {
        MainActivity.expendedAppBarLayout()
        initToolbar()
        super.onAttach(context)
    }

    private fun initToolbar() {
        supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar.apply {
            prevTitle = this?.title.toString()
            this?.title = "할 일 추가"
            this?.setDisplayHomeAsUpEnabled(true)
        }
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
        Thread { TodoModel.addTodo(binding.etTodoContents.text.toString()) }.start()
    }

    override fun onDetach() {
        Util.hideInput(requireActivity().currentFocus)
        super.onDetach()
    }

    override fun onDestroy() {
        supportActionBar?.title = prevTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onDestroy()
    }
}