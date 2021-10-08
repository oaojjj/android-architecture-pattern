package com.oaojjj.architecturepattern.frgment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oaojjj.architecturepattern.databinding.FragmentAddTodoBinding
import com.oaojjj.architecturepattern.listener.OnFinishedAddTodoListener


class AddTodoFragment : Fragment(), OnFinishedAddTodoListener {
    private lateinit var binding: FragmentAddTodoBinding

    override fun onAttach(context: Context) {
        requireActivity().title = "할 일 추가"
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onFinishedAddTodo() {

    }
}